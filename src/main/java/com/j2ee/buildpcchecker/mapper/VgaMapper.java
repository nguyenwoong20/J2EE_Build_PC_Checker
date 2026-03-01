package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.VgaCreationRequest;
import com.j2ee.buildpcchecker.dto.request.VgaUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.VgaResponse;
import com.j2ee.buildpcchecker.entity.Vga;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VgaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pcieVersion", ignore = true)
    @Mapping(target = "powerConnector", ignore = true)
    Vga toVga(VgaCreationRequest request);

    VgaResponse toVgaResponse(Vga vga);

    List<VgaResponse> toListVgaResponse(List<Vga> vgas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pcieVersion", ignore = true)
    @Mapping(target = "powerConnector", ignore = true)
    void updateVga(@MappingTarget Vga vga, VgaUpdateRequest request);
}

