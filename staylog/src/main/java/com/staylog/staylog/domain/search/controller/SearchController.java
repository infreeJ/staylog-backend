package com.staylog.staylog.domain.search.controller;

import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.search.dto.request.AccomListRequest;
import com.staylog.staylog.domain.search.dto.response.AccomListResponse;
import com.staylog.staylog.domain.search.mapper.SearchMapper;
import com.staylog.staylog.domain.search.service.SearchService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "AuthController", description = "인증/인가 API")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class SearchController {

    private final SearchMapper searchMapper;
    private final MessageUtil messageUtil;

    @GetMapping("/search/accommodations")
    public ResponseEntity<SuccessResponse<List<AccomListResponse>>> searchAccommodations(
            @ModelAttribute AccomListRequest request) {
        
        List<AccomListResponse> accomListResponse = searchMapper.getAccomList(request);
        String code = SuccessCode.SUCCESS.name();
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        SuccessResponse<List<AccomListResponse>> success = SuccessResponse.of(code, message, accomListResponse);

        return ResponseEntity.ok(success);

        //7654 1177
    }

}
