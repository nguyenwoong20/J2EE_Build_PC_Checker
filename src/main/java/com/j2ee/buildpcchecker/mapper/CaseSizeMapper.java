package com.j2ee.buildpcchecker.mapper;

import com.j2ee.buildpcchecker.dto.request.CaseSizeRequest;
import com.j2ee.buildpcchecker.dto.response.CaseSizeResponse;
import com.j2ee.buildpcchecker.entity.CaseSize;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaseSizeMapper {

    CaseSize toCaseSize(CaseSizeRequest request);

    CaseSizeResponse toCaseSizeResponse(CaseSize caseSize);

    List<CaseSizeResponse> toListCaseSizeResponse(List<CaseSize> caseSizes);

    void updateCaseSize(@MappingTarget CaseSize caseSize, CaseSizeRequest request);
}

