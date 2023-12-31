package com.example.backend.dto.inventory;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class InventoryListDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        List<InventoryDto> inventoryDtoList;
    }

}
