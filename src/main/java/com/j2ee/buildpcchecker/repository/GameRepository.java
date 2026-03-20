package com.j2ee.buildpcchecker.repository;

import com.j2ee.buildpcchecker.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    boolean existsByName(String name);

    Page<Game> findAllByGenreIgnoreCase(String genre, Pageable pageable);
}

