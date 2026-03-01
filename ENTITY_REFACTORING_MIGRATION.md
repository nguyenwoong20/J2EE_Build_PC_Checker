# Database Migration - Entity Refactoring

## Migration Date: 2026-02-21

## Overview
This migration refactors PC part entities to properly normalize relationships:
1. PSU: ManyToOne → ManyToMany with PcieConnector
2. Mainboard: String size → ManyToOne with CaseSize
3. VGA: String powerConnector → ManyToOne with PcieConnector

---

## Migration Steps

### 1. PSU Entity - ManyToMany with PcieConnector

#### Drop old column and create join table

```sql
-- Drop the old single pcie_connector_id column
ALTER TABLE psu DROP FOREIGN KEY IF EXISTS fk_psu_pcie_connector;
ALTER TABLE psu DROP COLUMN IF EXISTS pcie_connector_id;

-- Create join table for ManyToMany relationship
CREATE TABLE IF NOT EXISTS psu_pcie_connector (
    psu_id VARCHAR(255) NOT NULL,
    pcie_connector_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (psu_id, pcie_connector_id),
    FOREIGN KEY (psu_id) REFERENCES psu(id) ON DELETE CASCADE,
    FOREIGN KEY (pcie_connector_id) REFERENCES pcie_connector(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for better query performance
CREATE INDEX idx_psu_pcie_connector_psu ON psu_pcie_connector(psu_id);
CREATE INDEX idx_psu_pcie_connector_connector ON psu_pcie_connector(pcie_connector_id);
```

**Impact:**
- Old data: Existing PSU records with single pcie_connector_id will lose that relationship (need manual migration if data exists)
- New functionality: PSU can now have multiple power connectors (e.g., 2X8PIN + 12VHPWR)

---

### 2. Mainboard Entity - ManyToOne with CaseSize

#### Convert String size to foreign key

```sql
-- Step 1: Add new size_id column
ALTER TABLE mainboard ADD COLUMN size_id VARCHAR(255) AFTER ram_max_capacity;

-- Step 2: Migrate existing data (if any exists)
-- Map string values to CaseSize IDs
UPDATE mainboard SET size_id = 'ATX' WHERE size = 'ATX';
UPDATE mainboard SET size_id = 'mATX' WHERE size = 'mATX' OR size = 'Micro ATX';
UPDATE mainboard SET size_id = 'ITX' WHERE size = 'ITX' OR size = 'Mini ITX';

-- Step 3: Add foreign key constraint
ALTER TABLE mainboard 
ADD CONSTRAINT fk_mainboard_case_size 
FOREIGN KEY (size_id) REFERENCES case_size(id);

-- Step 4: Make size_id NOT NULL (after data migration)
ALTER TABLE mainboard MODIFY COLUMN size_id VARCHAR(255) NOT NULL;

-- Step 5: Drop old size column
ALTER TABLE mainboard DROP COLUMN size;

-- Create index for better query performance
CREATE INDEX idx_mainboard_size ON mainboard(size_id);
```

**Impact:**
- Old data: String values must be mapped to valid CaseSize IDs
- New functionality: Mainboard size is now properly normalized with referential integrity

---

### 3. VGA Entity - ManyToOne with PcieConnector

#### Convert String powerConnector to foreign key

```sql
-- Step 1: Add new power_connector_id column (nullable - some VGAs don't need external power)
ALTER TABLE vga ADD COLUMN power_connector_id VARCHAR(255) AFTER pcie_version_id;

-- Step 2: Migrate existing data (if any exists)
-- Map string values to PcieConnector IDs
UPDATE vga SET power_connector_id = '2X8PIN' WHERE power_connector LIKE '%2%8%pin%' OR power_connector = '2X8PIN';
UPDATE vga SET power_connector_id = '3X8PIN' WHERE power_connector LIKE '%3%8%pin%' OR power_connector = '3X8PIN';
UPDATE vga SET power_connector_id = '12VHPWR' WHERE power_connector LIKE '%12VHPWR%' OR power_connector = '12VHPWR';
UPDATE vga SET power_connector_id = '16PIN' WHERE power_connector LIKE '%16%pin%' OR power_connector = '16PIN';
UPDATE vga SET power_connector_id = '6PIN' WHERE power_connector LIKE '%6%pin%' OR power_connector = '6PIN';
UPDATE vga SET power_connector_id = '8PIN' WHERE power_connector LIKE '%8%pin%' AND power_connector NOT LIKE '%2%' AND power_connector NOT LIKE '%3%';

-- Step 3: Add foreign key constraint (with NULL allowed)
ALTER TABLE vga 
ADD CONSTRAINT fk_vga_power_connector 
FOREIGN KEY (power_connector_id) REFERENCES pcie_connector(id);

-- Step 4: Drop old power_connector column
ALTER TABLE vga DROP COLUMN power_connector;

-- Create index for better query performance
CREATE INDEX idx_vga_power_connector ON vga(power_connector_id);
```

**Impact:**
- Old data: String values must be mapped to valid PcieConnector IDs
- New functionality: VGA power connector is now properly normalized
- Nullable: Some VGAs don't require external power (low-end cards)

---

## Rollback Scripts

### If migration fails, use these rollback scripts:

#### Rollback PSU
```sql
-- Drop join table
DROP TABLE IF EXISTS psu_pcie_connector;

-- Re-add old column
ALTER TABLE psu ADD COLUMN pcie_connector_id VARCHAR(255) AFTER efficiency;
ALTER TABLE psu ADD CONSTRAINT fk_psu_pcie_connector 
FOREIGN KEY (pcie_connector_id) REFERENCES pcie_connector(id);
```

#### Rollback Mainboard
```sql
-- Add back old column
ALTER TABLE mainboard ADD COLUMN size VARCHAR(255) NOT NULL AFTER ram_max_capacity;

-- Migrate data back
UPDATE mainboard SET size = size_id;

-- Drop new column and constraint
ALTER TABLE mainboard DROP FOREIGN KEY fk_mainboard_case_size;
ALTER TABLE mainboard DROP COLUMN size_id;
```

#### Rollback VGA
```sql
-- Add back old column
ALTER TABLE vga ADD COLUMN power_connector VARCHAR(255) AFTER pcie_version_id;

-- Migrate data back
UPDATE vga SET power_connector = power_connector_id WHERE power_connector_id IS NOT NULL;

-- Drop new column and constraint
ALTER TABLE vga DROP FOREIGN KEY fk_vga_power_connector;
ALTER TABLE vga DROP COLUMN power_connector_id;
```

---

## Data Migration Considerations

### Before Migration:
1. **Backup database**: `mysqldump -u root -p identity_service > backup_before_migration.sql`
2. **Check existing data**:
   ```sql
   SELECT DISTINCT size FROM mainboard;
   SELECT DISTINCT power_connector FROM vga;
   SELECT pcie_connector_id, COUNT(*) FROM psu GROUP BY pcie_connector_id;
   ```
3. **Ensure reference data exists** in `case_size` and `pcie_connector` tables

### After Migration:
1. **Verify data integrity**:
   ```sql
   -- Check all mainboards have valid size
   SELECT COUNT(*) FROM mainboard WHERE size_id IS NULL;
   
   -- Check foreign keys
   SELECT COUNT(*) FROM mainboard m 
   LEFT JOIN case_size cs ON m.size_id = cs.id 
   WHERE cs.id IS NULL;
   ```
2. **Test application** with new entity structure
3. **Update application code** to use new relationships

---

## Application Code Changes

### PSU
- **Entity**: `Set<PcieConnector> pcieConnectors` with `@ManyToMany` and `@JoinTable`
- **Request DTO**: `Set<String> pcieConnectorIds`
- **Response DTO**: `Set<PcieConnectorResponse> pcieConnectors`
- **Service**: Loop through IDs to fetch and set connectors

### Mainboard
- **Entity**: `CaseSize size` with `@ManyToOne` and `@JoinColumn(name = "size_id")`
- **Request DTO**: `String sizeId`
- **Response DTO**: `CaseSizeResponse size`
- **Service**: Fetch CaseSize by ID and set to mainboard

### VGA
- **Entity**: `PcieConnector powerConnector` with `@ManyToOne` and `@JoinColumn(name = "power_connector_id")`
- **Request DTO**: `String powerConnectorId` (nullable)
- **Response DTO**: `PcieConnectorResponse powerConnector`
- **Service**: Optionally fetch PcieConnector if ID provided

---

## Testing Checklist

- [ ] PSU CRUD operations work with multiple PCIe connectors
- [ ] Mainboard CRUD operations work with CaseSize relationship
- [ ] VGA CRUD operations work with optional PcieConnector
- [ ] Swagger UI displays updated request/response structures
- [ ] GET endpoints return nested objects correctly
- [ ] Foreign key constraints prevent invalid data
- [ ] Cascade deletes work as expected
- [ ] Performance is acceptable with joins

---

## Notes

1. **PSU ManyToMany**: Allows PSU to have multiple connector types (realistic: modern PSUs have various connectors)
2. **Mainboard size**: Now properly normalized, prevents typos like "ATX " vs "ATX"
3. **VGA powerConnector**: Optional relationship (some low-end VGAs don't need external power)
4. **Backward compatibility**: API contracts changed - clients need to update
5. **Database indexes**: Added for better query performance on foreign keys

---

**Migration Status**: Ready to execute  
**Estimated Time**: 5-10 minutes (depends on data volume)  
**Risk Level**: Medium (requires data migration)  
**Rollback Available**: Yes

