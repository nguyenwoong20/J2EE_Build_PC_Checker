package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.GameCreationRequest;
import com.j2ee.buildpcchecker.dto.request.GameUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.GameResponse;
import com.j2ee.buildpcchecker.dto.response.GameSummaryResponse;
import com.j2ee.buildpcchecker.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "id", ignore = true)
    Game toGame(GameCreationRequest request);

    GameResponse toGameResponse(Game game);

    GameSummaryResponse toGameSummaryResponse(Game game);

    List<GameResponse> toListGameResponse(List<Game> games);

    List<GameSummaryResponse> toListGameSummaryResponse(List<Game> games);

    @Mapping(target = "id", ignore = true)
    void updateGame(@MappingTarget Game game, GameUpdateRequest request);
}

