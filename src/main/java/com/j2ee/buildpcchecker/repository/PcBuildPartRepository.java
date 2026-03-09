package com.j2ee.buildpcchecker.repository;

import com.j2ee.buildpcchecker.entity.PcBuildPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PcBuildPartRepository extends JpaRepository<PcBuildPart, String> {
}

