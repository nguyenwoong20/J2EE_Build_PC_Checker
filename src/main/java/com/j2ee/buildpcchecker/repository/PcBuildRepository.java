package com.j2ee.buildpcchecker.repository;

import com.j2ee.buildpcchecker.entity.PcBuild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PcBuildRepository extends JpaRepository<PcBuild, String> {
    List<PcBuild> findByUserId(String userId);

    boolean existsByUserIdAndName(String userId, String name);
}
