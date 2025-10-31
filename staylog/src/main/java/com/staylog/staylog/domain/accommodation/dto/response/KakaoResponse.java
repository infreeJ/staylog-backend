package com.staylog.staylog.domain.accommodation.dto.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/*
 * 정나영
 * 카카오 api응답을 json을 담기 위함
 * */

@Getter
@Setter
public class KakaoResponse { 
    private List<Document> documents;

    @Data
    public static class Document {
        private String x;
        private String y;
    }
}