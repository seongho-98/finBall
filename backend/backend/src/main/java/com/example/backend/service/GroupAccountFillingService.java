package com.example.backend.service;

import com.example.backend.dto.RestDto;
import com.example.backend.dto.transfer.AccountTransferDto.Request;
import com.example.backend.dto.transfer.FinBallTradeHistoryDto;
import com.example.backend.dto.transfer.TransferInfoDto;
import com.example.backend.entity.FinBallAccount;
import com.example.backend.entity.GroupAccount;
import com.example.backend.entity.Member;
import com.example.backend.error.ErrorCode;
import com.example.backend.exception.CustomException;
import com.example.backend.repository.finballaccount.FinBallAccountRepository;
import com.example.backend.repository.finballhistory.FinBallHistoryRepository;
import com.example.backend.repository.groupaccount.GroupAccountRepository;
import com.example.backend.repository.groupaccounthistory.GroupAccountHistoryRepository;
import com.example.backend.util.RedisUtil;
import com.example.backend.util.RestTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupAccountFillingService {

    private final Long FIN_BALL_CODE = 106L;

    private final GroupAccountRepository groupAccountRepository;
    private final FinBallAccountRepository finBallAccountRepository;
    private final FinBallHistoryRepository finBallHistoryRepository;
    private final GroupAccountHistoryRepository groupAccountHistoryRepository;

    private final RestTemplateUtil restTemplateUtil;
    private final RedisUtil redisUtil;

    public void transferGroupAccount(Request request, Member member)
            throws JsonProcessingException {

        GroupAccount groupAccount = getGroupAccount(request); //모임 계좌 있는지 체크

        init(request, groupAccount);
        List<FinBallTradeHistoryDto> historyDtoList = getMyDataResponse(request,
                member.getUserId());
        save(historyDtoList, request.getPlusBank().getAccountNumber()); //plusBank : 그룹 통장
    }

    public void init(Request request, GroupAccount groupAccount) {
        TransferInfoDto plus = request.getPlusBank();
        TransferInfoDto minus = request.getMinusBank();

        // 모임통장 전처리
        plus.setCode(FIN_BALL_CODE);
        plus.setTarget(groupAccount.getName());
        plus.setBalance(groupAccount.getBalance());

        if (Objects.equals(minus.getCode(), FIN_BALL_CODE)) { // 내 은행이 핀볼 은행이라면
            request.getMinusBank().setBalance(getAccountBalance(minus, request.getValue()));
        }
    }

    public Long getAccountBalance(TransferInfoDto info, Long value) {
        FinBallAccount finBallAccount = finBallAccountRepository.findById(info.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 계좌가 존재하지 않습니다."));

        Long balance = finBallAccount.getBalance();
        if (balance < value) { //보낼 금액이 작은 상황
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }
        return balance;
    }

    private GroupAccount getGroupAccount(Request request) {

        // 요청한 그룹 계좌가 있는지 조회
        GroupAccount groupAccount = groupAccountRepository.findById(
                request.getPlusBank().getAccountNumber()).orElseThrow(
                () -> new CustomException(ErrorCode.GROUP_ACCOUNT_NOT_FOUND)
        );

        return groupAccount;
    }

    public List<FinBallTradeHistoryDto> getMyDataResponse(Request request,
            String memberId)
            throws JsonProcessingException {
        String token = redisUtil.getMyDataToken(memberId);

        ResponseEntity<String> response = restTemplateUtil.callMyData(token,
                request, "/myData/transfer",
                HttpMethod.POST);

        RestDto<FinBallTradeHistoryDto> restDto = new RestDto<>(FinBallTradeHistoryDto.class,
                response);

        return (List<FinBallTradeHistoryDto>) restTemplateUtil.parseListBody(
                restDto, "list");
    }

    @Transactional
    public void save(List<FinBallTradeHistoryDto> historyDtoList, String groupAccountNumber) {

        for (FinBallTradeHistoryDto historyDto : historyDtoList) {
            // plusAccount 처리(그룹Account)
            if (historyDto.getAccountNumber().equals(groupAccountNumber)) {
                saveGroupAccount(historyDto);
            } else {
                // minusAccount가 핀볼 계좌인 경우에 대한 처리
                saveFinBallAccount(historyDto);
            }
        }
    }

    @Transactional
    public void saveFinBallAccount(FinBallTradeHistoryDto historyDto) {

        FinBallAccount finBallAccount = finBallAccountRepository.findById(
                        historyDto.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계좌가 없습니다."));

        finBallAccount.setBalance(historyDto.getRemain());
        finBallAccountRepository.save(finBallAccount);
        finBallHistoryRepository.save(historyDto.toFinBallHistory(finBallAccount));
    }

    @Transactional
    public void saveGroupAccount(FinBallTradeHistoryDto historyDto) {
        GroupAccount groupAccount = groupAccountRepository.findById(
                        historyDto.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계좌가 없습니다."));

        groupAccount.setBalance(historyDto.getRemain());
        groupAccountRepository.save(groupAccount);
        groupAccountHistoryRepository.save(historyDto.toGroupAccountHistory(groupAccount));
    }
}
