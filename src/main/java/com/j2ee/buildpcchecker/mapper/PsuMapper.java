package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.PsuCreationRequest;
import com.j2ee.buildpcchecker.dto.request.PsuUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.PsuResponse;
import com.j2ee.buildpcchecker.entity.Psu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PsuMapper {

    @Mapping(target = "pcieConnectors", ignore = true) // Will be set manually in service
    @Mapping(target = "id", ignore = true)
    Psu toPsu(PsuCreationRequest request);

    PsuResponse toPsuResponse(Psu psu);

    List<PsuResponse> toListPsuResponse(List<Psu> psus);
    
    @Mapping(target = "pcieConnectors", ignore = true) // Will be set manually in service if needed
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updatePsu(@MappingTarget Psu psu, PsuUpdateRequest request);
}
