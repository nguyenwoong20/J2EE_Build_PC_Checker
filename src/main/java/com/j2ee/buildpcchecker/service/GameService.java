package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.GameCreationRequest;
import com.j2ee.buildpcchecker.dto.request.GameUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.GameResponse;
import com.j2ee.buildpcchecker.dto.response.GameSummaryResponse;
import com.j2ee.buildpcchecker.dto.response.PageResponse;
import com.j2ee.buildpcchecker.entity.Game;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.GameMapper;
import com.j2ee.buildpcchecker.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GameService {

    GameRepository gameRepository;
    GameMapper gameMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public GameResponse createGame(GameCreationRequest request) {
        log.info("Creating new Game: {}", request.getName());

        if (gameRepository.existsByName(request.getName())) {
            log.error("Game name already exists: {}", request.getName());
            throw new AppException(ErrorCode.GAME_NAME_ALREADY_EXISTS);
        }

        Game game = gameMapper.toGame(request);
        Game saved = gameRepository.save(game);
        return gameMapper.toGameResponse(saved);
    }

    public List<GameResponse> getAllGames() {
        log.info("Getting all Games");
        return gameMapper.toListGameResponse(gameRepository.findAll());
    }

    /**
     * Get games with pagination and optional genre filter.
     *
     * @param genre optional genre filter (ignore case)
     * @param page  zero-based page index
     * @param size  page size
     */
    public PageResponse<GameSummaryResponse> getGames(String genre, int page, int size) {
        log.info("Getting games - genre: {}, page: {}, size: {}", genre, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> gamePage = (genre != null && !genre.isBlank())
                ? gameRepository.findAllByGenreIgnoreCase(genre.trim(), pageable)
                : gameRepository.findAll(pageable);
        List<GameSummaryResponse> content = gameMapper.toListGameSummaryResponse(gamePage.getContent());
        return PageResponse.<GameSummaryResponse>builder()
                .content(content)
                .totalElements(gamePage.getTotalElements())
                .totalPages(gamePage.getTotalPages())
                .size(gamePage.getSize())
                .number(gamePage.getNumber())
                .build();
    }

    public GameResponse getGameById(String gameId) {
        log.info("Getting Game with ID: {}", gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.error("Game not found with ID: {}", gameId);
                    return new AppException(ErrorCode.GAME_NOT_FOUND);
                });

        return gameMapper.toGameResponse(game);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public GameResponse updateGame(GameUpdateRequest request, String gameId) {
        log.info("Updating Game with ID: {}", gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.error("Game not found with ID: {}", gameId);
                    return new AppException(ErrorCode.GAME_NOT_FOUND);
                });

        if (request.getName() != null && !request.getName().equalsIgnoreCase(game.getName())
                && gameRepository.existsByName(request.getName())) {
            log.error("Game name already exists: {}", request.getName());
            throw new AppException(ErrorCode.GAME_NAME_ALREADY_EXISTS);
        }

        gameMapper.updateGame(game, request);

        Game updated = gameRepository.save(game);
        return gameMapper.toGameResponse(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGame(String gameId) {
        log.info("Deleting Game with ID: {}", gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> {
                    log.error("Game not found with ID: {}", gameId);
                    return new AppException(ErrorCode.GAME_NOT_FOUND);
                });

        gameRepository.delete(game);
    }
}

