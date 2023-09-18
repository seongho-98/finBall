package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.dto.inventory.InventoryDto;
import com.example.backend.security.UserDetailsImpl;
import com.example.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("ball/inventory")
    public Response<InventoryDto.Response> getInventory(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userId = userDetails.getUsername();
        InventoryDto.Response response = inventoryService.getInventory(userId);

        return new Response(200, "보유중인 스킨 목록을 조회하였습니다.", response);
    }


}