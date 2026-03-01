package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.MainboardCreationRequest;
import com.j2ee.buildpcchecker.dto.request.MainboardUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.MainboardResponse;
import com.j2ee.buildpcchecker.entity.Mainboard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MainboardMapper {

    @Mapping(target = "socket", ignore = true)
    @Mapping(target = "ramType", ignore = true)
    @Mapping(target = "pcieVgaVersion", ignore = true)
    @Mapping(target = "size", ignore = true)
    Mainboard toMainboard(MainboardCreationRequest request);

    MainboardResponse toMainboardResponse(Mainboard mainboard);

    List<MainboardResponse> toListMainboardResponse(List<Mainboard> mainboards);

    @Mapping(target = "socket", ignore = true)
    @Mapping(target = "ramType", ignore = true)
    @Mapping(target = "pcieVgaVersion", ignore = true)
    @Mapping(target = "size", ignore = true)
    void updateMainboard(@MappingTarget Mainboard mainboard, MainboardUpdateRequest request);
}



