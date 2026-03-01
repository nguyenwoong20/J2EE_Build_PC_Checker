package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.CaseCreationRequest;
import com.j2ee.buildpcchecker.dto.request.CaseUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.CaseResponse;
import com.j2ee.buildpcchecker.entity.PcCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaseMapper {

    @Mapping(target = "size", ignore = true)
    PcCase toPcCase(CaseCreationRequest request);

    CaseResponse toCaseResponse(PcCase pcCase);

    List<CaseResponse> toListCaseResponse(List<PcCase> pcCases);
    
    @Mapping(target = "size", ignore = true)
    void updatePcCase(@MappingTarget PcCase pcCase, CaseUpdateRequest request);
}