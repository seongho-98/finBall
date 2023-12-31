package com.finball.mydata.dto.card;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class CardListDto {

    @Data
    public static class Request {
        List<Long> cardCompanyCodeList;
    }

    @Data
    @Builder
    public static class Response {
        List<CardDto> cardList;
    }
}
