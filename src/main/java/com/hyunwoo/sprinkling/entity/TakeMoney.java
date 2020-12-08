package com.hyunwoo.sprinkling.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "unique_sprinkling_id_receive_user_id", columnList = "sprinkling_id, receiveUserId", unique = true)
})
public class TakeMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkling_id")
    private Sprinkling sprinkling;

    @Column(nullable = false)
    private Long amount;

    private Long receiveUserId;
    private LocalDateTime receiveTime;

    public TakeMoney(Sprinkling sprinkling, Long receiveUserId, Long amount){
        this.sprinkling = sprinkling;
        this.receiveUserId = receiveUserId;
        this.amount = amount;
    }

    public long take(Long receiveUserId){
        this.receiveUserId = receiveUserId;
        this.receiveTime = LocalDateTime.now();
        return amount;
    }
}
