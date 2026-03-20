package com.j2ee.buildpcchecker.configuration;

import com.j2ee.buildpcchecker.entity.Game;
import com.j2ee.buildpcchecker.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GameDataSeeder {

    GameRepository gameRepository;

    @Bean
    CommandLineRunner seedGames() {
        return args -> {
            if (gameRepository.count() > 0) {
                return;
            }

            // Scores are PassMark-style rough values used for demo/testing.
            // Admin can later adjust these via /games CRUD.
            List<Game> games = List.of(
                    Game.builder().name("Cyberpunk 2077").genre("RPG/Action")
                            .minCpuScore(9000).minGpuScore(8000).recCpuScore(16000).recGpuScore(17000)
                            .minRamGb(12).recRamGb(16)
                            .baseFpsLow(45).baseFpsMedium(60).baseFpsHigh(60)
                            .build(),
                    Game.builder().name("Elden Ring").genre("Action/RPG")
                            .minCpuScore(7000).minGpuScore(7000).recCpuScore(12000).recGpuScore(12000)
                            .minRamGb(12).recRamGb(16)
                            .baseFpsLow(60).baseFpsMedium(60).baseFpsHigh(60)
                            .build(),
                    Game.builder().name("GTA V").genre("Action")
                            .minCpuScore(4500).minGpuScore(3500).recCpuScore(7000).recGpuScore(7000)
                            .minRamGb(8).recRamGb(8)
                            .baseFpsLow(60).baseFpsMedium(90).baseFpsHigh(90)
                            .build(),
                    Game.builder().name("Red Dead Redemption 2").genre("Action")
                            .minCpuScore(8000).minGpuScore(7000).recCpuScore(14000).recGpuScore(14000)
                            .minRamGb(12).recRamGb(16)
                            .baseFpsLow(45).baseFpsMedium(60).baseFpsHigh(60)
                            .build(),
                    Game.builder().name("Hogwarts Legacy").genre("RPG/Action")
                            .minCpuScore(8000).minGpuScore(8000).recCpuScore(15000).recGpuScore(16000)
                            .minRamGb(16).recRamGb(16)
                            .baseFpsLow(45).baseFpsMedium(60).baseFpsHigh(60)
                            .build(),
                    Game.builder().name("Valorant").genre("FPS")
                            .minCpuScore(3000).minGpuScore(2000).recCpuScore(6000).recGpuScore(4000)
                            .minRamGb(4).recRamGb(8)
                            .baseFpsLow(120).baseFpsMedium(180).baseFpsHigh(200)
                            .build(),
                    Game.builder().name("CS2").genre("FPS")
                            .minCpuScore(4500).minGpuScore(3000).recCpuScore(9000).recGpuScore(6000)
                            .minRamGb(8).recRamGb(16)
                            .baseFpsLow(120).baseFpsMedium(160).baseFpsHigh(180)
                            .build(),
                    Game.builder().name("Apex Legends").genre("FPS")
                            .minCpuScore(5500).minGpuScore(4500).recCpuScore(10000).recGpuScore(9000)
                            .minRamGb(8).recRamGb(16)
                            .baseFpsLow(90).baseFpsMedium(120).baseFpsHigh(120)
                            .build(),
                    Game.builder().name("The Witcher 3").genre("RPG")
                            .minCpuScore(4000).minGpuScore(3500).recCpuScore(8000).recGpuScore(7000)
                            .minRamGb(8).recRamGb(8)
                            .baseFpsLow(60).baseFpsMedium(90).baseFpsHigh(90)
                            .build(),
                    Game.builder().name("Fortnite").genre("Battle Royale")
                            .minCpuScore(4500).minGpuScore(3500).recCpuScore(9000).recGpuScore(8000)
                            .minRamGb(8).recRamGb(16)
                            .baseFpsLow(90).baseFpsMedium(120).baseFpsHigh(120)
                            .build(),
                    Game.builder().name("Forza Horizon 5").genre("Racing")
                            .minCpuScore(7000).minGpuScore(6000).recCpuScore(12000).recGpuScore(12000)
                            .minRamGb(8).recRamGb(16)
                            .baseFpsLow(60).baseFpsMedium(90).baseFpsHigh(90)
                            .build(),
                    Game.builder().name("God of War").genre("Action")
                            .minCpuScore(8000).minGpuScore(7000).recCpuScore(14000).recGpuScore(14000)
                            .minRamGb(8).recRamGb(16)
                            .baseFpsLow(60).baseFpsMedium(75).baseFpsHigh(75)
                            .build()
            );

            gameRepository.saveAll(games);
            log.info("Seeded {} games", games.size());
        };
    }
}

