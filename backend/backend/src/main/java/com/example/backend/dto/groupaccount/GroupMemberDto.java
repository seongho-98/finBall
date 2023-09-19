package com.example.backend.dto.groupaccount;

import com.example.backend.type.HostType;
import lombok.Data;

@Data
public class GroupMemberDto {
    private String name;
    private String profileImage;
    private long value;
    private long balance;
    private long percent;
    private HostType type;
    private long skinId;
}