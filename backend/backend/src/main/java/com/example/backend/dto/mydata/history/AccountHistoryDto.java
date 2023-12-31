package com.example.backend.dto.mydata.history;

import com.example.backend.dto.transfer.OppositeDto;
import com.example.backend.entity.FinBallHistory;
import com.example.backend.type.DealType;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryDto {

    private Long id;
    private String accountNo;
    private Long value;
    private LocalDate date;
    private LocalTime time;
    private DealType type;
    private Long balance;
    private OppositeDto opposite;

    public AccountHistoryDto(FinBallHistory finBallHistory) {
        this.id = finBallHistory.getId();
        this.accountNo = finBallHistory.getFinBallAccount().getAccountNo();
        this.value = finBallHistory.getValue();
        this.date = finBallHistory.getDate().toLocalDate();
        this.time = finBallHistory.getDate().toLocalTime();
        this.type = finBallHistory.getDealType();
        this.balance = finBallHistory.getBalance();
        this.opposite = new OppositeDto(finBallHistory);
    }

}
