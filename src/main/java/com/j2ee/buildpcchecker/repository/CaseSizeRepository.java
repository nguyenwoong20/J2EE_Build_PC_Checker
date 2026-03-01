package com.j2ee.buildpcchecker.repository;

import com.j2ee.buildpcchecker.entity.CaseSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseSizeRepository extends JpaRepository<CaseSize, String> {
}

