package com.staylog.staylog.domain.search.mapper;

import com.staylog.staylog.domain.search.dto.request.AccomListRequest;
import com.staylog.staylog.domain.search.dto.response.AccomListResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<AccomListResponse> getAccomList(AccomListRequest request);
}
