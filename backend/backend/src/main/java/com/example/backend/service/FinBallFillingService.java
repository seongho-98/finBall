package com.example.backend.service;

import com.example.backend.dto.RestDto;
import com.example.backend.dto.finball.ReadFinBallHistoryDto.Response;
import com.example.backend.dto.transfer.AccountTransferDto.Request;
import com.example.backend.dto.transfer.FinBallTradeHistoryDto;
import com.example.backend.dto.transfer.TransferInfoDto;
import com.example.backend.entity.FinBallAccount;
import com.example.backend.entity.Member;
import com.example.backend.error.ErrorCode;
import com.example.backend.exception.CustomException;
import com.example.backend.repository.finballaccount.FinBallAccountRepository;
import com.example.backend.repository.finballhistory.FinBallHistoryRepository;
import com.example.backend.util.RedisUtil;
import com.example.backend.util.RestTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinBallFillingService {

    private final Long FIN_BALL_CODE = 106L;

    private final FinBallAccountRepository finBallAccountRepository;
    private final FinBallHistoryRepository finBallHistoryRepository;
    private final FinBallService finBallService;

    private final RestTemplateUtil restTemplateUtil;
    private final RedisUtil redisUtil;

    public Response fillingFinBallAccount(Request request, Member member)
            throws JsonProcessingException {

        FinBallAccount finBallAccount = getFinBallAccount(request, member); //모임 계좌 있는지 체크

        init(request, finBallAccount);
        List<FinBallTradeHistoryDto> historyDtoList = getMyDataResponse(request,
                member.getUserId());
        save(historyDtoList, request.getPlusBank().getAccountNo()); //plusBank : 그룹 통장

        return finBallService.readFinBallHistoryList(member);
    }

    private FinBallAccount getFinBallAccount(Request request, Member member) {
        FinBallAccount finBallAccount = finBallAccountRepository.findById(
                request.getPlusBank().getAccountNo()).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );

        if (finBallAccount.getMember().getId() != member.getId()) {
            throw new CustomException(ErrorCode.OWNER_NOT_CORRESPOND);
        }

        return finBallAccount;
    }

    public void init(Request request, FinBallAccount finBallAccount) {
        TransferInfoDto plus = request.getPlusBank();
//        TransferInfoDto minus = request.getMinusBank();

        // 핀볼 통장 전처리
        plus.setCompanyId(FIN_BALL_CODE);
        plus.setUserName(finBallAccount.getMember().getName());
        plus.setBalance(finBallAccount.getBalance());

//        if (Objects.equals(minus.getCompanyId(), FIN_BALL_CODE)) { // minus bank가 핀볼 은행이라면
//            request.getMinusBank().setBalance(getAccountBalance(minus, request.getValue()));
//        }
    }

    public Long getAccountBalance(TransferInfoDto info, Long value) {
        FinBallAccount finBallAccount = finBallAccountRepository.findById(info.getAccountNo())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 계좌가 존재하지 않습니다."));

        Long balance = finBallAccount.getBalance();
        if (balance < value) { //보낼 금액이 작은 상황
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }
        return balance;
    }


    public List<FinBallTradeHistoryDto> getMyDataResponse(Request request,
            String memberId)
            throws JsonProcessingException {
        String token = redisUtil.getMyDataToken(memberId);

        ResponseEntity<String> response = restTemplateUtil.callMyData(token,
                request, "/my-data/transfer",
                HttpMethod.POST);

        RestDto<FinBallTradeHistoryDto> restDto = new RestDto<>(FinBallTradeHistoryDto.class,
                response);

        return (List<FinBallTradeHistoryDto>) restTemplateUtil.parseListBody(
                restDto, "list");
    }

    @Transactional
    public void save(List<FinBallTradeHistoryDto> historyDtoList, String myAccountNumber) {

        for (FinBallTradeHistoryDto historyDto : historyDtoList) {
            // plusAccount 처리(내 핀볼 계좌)
            saveFinBallAccount(historyDto);
        }
    }

    @Transactional
    public void saveFinBallAccount(FinBallTradeHistoryDto historyDto) {

        FinBallAccount finBallAccount = finBallAccountRepository.findById(
                        historyDto.getAccountNo())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 계좌가 없습니다."));

        finBallAccount.setBalance(historyDto.getBalance());
        finBallAccountRepository.save(finBallAccount);
        finBallHistoryRepository.save(historyDto.toFinBallHistory(finBallAccount));
    }

}
