package com.hyunwoo.sprinkling.model;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneySprinkling {
    private Long userId;
    private String roomId;
    private long amount;
    private long targetCount;

}
