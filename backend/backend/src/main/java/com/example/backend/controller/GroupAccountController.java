package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.dto.groupaccount.AcceptGroupAccountDto;
import com.example.backend.dto.groupaccount.DeleteGroupAccountDto;
import com.example.backend.dto.groupaccount.GameEndDto;
import com.example.backend.dto.groupaccount.GroupAccountDto;
import com.example.backend.dto.groupaccount.RegistGroupAccountDto;
import com.example.backend.dto.transfer.AccountTransferDto;
import com.example.backend.entity.Member;
import com.example.backend.security.UserDetailsImpl;
import com.example.backend.service.GroupAccountService;
import com.example.backend.service.GroupAccountTransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class GroupAccountController {

    private final GroupAccountService groupAccountService;
    private final GroupAccountTransferService groupAccountTransferService; //이체 처리 service

    @GetMapping("/group/account/{groupAccountId}")
    public Response<GroupAccountDto.Response> getGroupAccount(@PathVariable String groupAccountId) {
        GroupAccountDto.Response response = groupAccountService.findByGroupAccountId(
                groupAccountId);
        return new Response<>(200, "그룹 계좌 조회 완료", response);
    }

    @PostMapping("/group/account")
    public Response registGroupAccount(@RequestBody RegistGroupAccountDto.Request request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        String response = groupAccountService.save(request, member);
        return new Response<>(201, "계좌 생성 완료", response);
    }

    @PostMapping("/group/account/invite/agree")
    public Response<AcceptGroupAccountDto.Response> acceptInvite(
            @RequestBody AcceptGroupAccountDto.Request request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        AcceptGroupAccountDto.Response response = groupAccountService.acceptInvite(request, member);
        return new Response<>(201, "그룹 계좌 가입 완료", response);
    }

    @PostMapping("/group/account/adjustment")
    public Response endGame(@RequestBody GameEndDto.Request request) {
        groupAccountService.endGame(request);
        return new Response<>(201, "게임 결과 반영 완료");
    }

    @PostMapping("/group/account/transfer") //이체 처리 API
    public Response transferGroupAccount(@RequestBody AccountTransferDto.Request request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {

        Member member = userDetails.getMember();
        groupAccountTransferService.transferGroupAccount(request, member);

        return new Response<>(200, "이체가 완료되었습니다.");
    }

    @DeleteMapping("/group/account")
    public Response deleteGroupAccount(@RequestBody DeleteGroupAccountDto.Request request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        Member member = userDetails.getMember();
        groupAccountService.delete(request, member);
        return new Response<>(204, "그룹 계좌 삭제 완료");

    }
}
