-- PC Build Save Feature - Database Migration Script
-- Date: 2026-03-09
-- Description: Create tables for storing user PC build configurations

-- =====================================================
-- Table: pc_build
-- Description: Stores PC build configurations created by users
-- =====================================================
CREATE TABLE IF NOT EXISTS pc_build (
    id VARCHAR(36) PRIMARY KEY COMMENT 'UUID primary key',
    name VARCHAR(255) NOT NULL COMMENT 'Build name',
    description TEXT COMMENT 'Build description',
    total_tdp INT COMMENT 'Total TDP of the build',
    created_at DATETIME COMMENT 'Timestamp when build was created',
    user_id VARCHAR(36) NOT NULL COMMENT 'Foreign key to user table',

    CONSTRAINT fk_pcbuild_user
        FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE,

    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='PC Build configurations table';

-- =====================================================
-- Table: pc_build_part
-- Description: Stores individual parts that belong to a PC build
-- =====================================================
CREATE TABLE IF NOT EXISTS pc_build_part (
    id VARCHAR(36) PRIMARY KEY COMMENT 'UUID primary key',
    part_type VARCHAR(50) NOT NULL COMMENT 'Type of part (CPU, MAINBOARD, RAM, GPU, PSU, CASE, COOLER, SSD, HDD)',
    part_id VARCHAR(36) NOT NULL COMMENT 'UUID reference to the actual part entity',
    quantity INT NOT NULL DEFAULT 1 COMMENT 'Quantity of this part',
    build_id VARCHAR(36) NOT NULL COMMENT 'Foreign key to pc_build table',

    CONSTRAINT fk_pcbuildpart_build
        FOREIGN KEY (build_id)
        REFERENCES pc_build(id)
        ON DELETE CASCADE,

    INDEX idx_build_id (build_id),
    INDEX idx_part_type (part_type),
    INDEX idx_part_id (part_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='PC Build parts junction table';

-- =====================================================
-- Sample Data (Optional - for testing)
-- =====================================================

-- Note: Replace UUIDs with actual IDs from your database
-- Note: Replace user_id with an actual user ID from your user table

/*
INSERT INTO pc_build (id, name, description, total_tdp, created_at, user_id)
VALUES (
    '7c9e8b5a-1234-5678-90ab-cdef12345678',
    'Gaming Build 2026',
    'Intel i9 + RTX 4090 Gaming Setup',
    NULL,
    NOW(),
    '{actual-user-uuid}'
);

INSERT INTO pc_build_part (id, part_type, part_id, quantity, build_id)
VALUES
    (UUID(), 'CPU', '{actual-cpu-uuid}', 1, '7c9e8b5a-1234-5678-90ab-cdef12345678'),
    (UUID(), 'MAINBOARD', '{actual-mainboard-uuid}', 1, '7c9e8b5a-1234-5678-90ab-cdef12345678'),
    (UUID(), 'RAM', '{actual-ram-uuid}', 1, '7c9e8b5a-1234-5678-90ab-cdef12345678'),
    (UUID(), 'GPU', '{actual-gpu-uuid}', 1, '7c9e8b5a-1234-5678-90ab-cdef12345678'),
    (UUID(), 'PSU', '{actual-psu-uuid}', 1, '7c9e8b5a-1234-5678-90ab-cdef12345678');
*/

-- =====================================================
-- Queries for Testing
-- =====================================================

-- Get all builds for a specific user
-- SELECT * FROM pc_build WHERE user_id = '{user-uuid}';

-- Get a build with all its parts
-- SELECT
--     b.id, b.name, b.description, b.created_at,
--     p.part_type, p.part_id, p.quantity
-- FROM pc_build b
-- LEFT JOIN pc_build_part p ON b.id = p.build_id
-- WHERE b.id = '{build-uuid}';

-- Count builds per user
-- SELECT user_id, COUNT(*) as build_count
-- FROM pc_build
-- GROUP BY user_id;

-- Get most recent builds
-- SELECT * FROM pc_build
-- ORDER BY created_at DESC
-- LIMIT 10;

-- =====================================================
-- Rollback Script (if needed)
-- =====================================================

-- DROP TABLE IF EXISTS pc_build_part;
-- DROP TABLE IF EXISTS pc_build;

-- =====================================================
-- Notes
-- =====================================================

-- 1. Hibernate with ddl-auto=update will create these tables automatically
-- 2. This script is provided for reference and manual database setup
-- 3. Foreign key constraints ensure data integrity
-- 4. ON DELETE CASCADE ensures parts are deleted when build is deleted
-- 5. Indexes improve query performance for filtering and joining

