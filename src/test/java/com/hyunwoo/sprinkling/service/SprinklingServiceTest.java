package com.hyunwoo.sprinkling.service;

import com.hyunwoo.sprinkling.entity.Sprinkling;
import com.hyunwoo.sprinkling.entity.TakeMoney;
import com.hyunwoo.sprinkling.model.MoneySprinkling;
import com.hyunwoo.sprinkling.repository.SprinklingRepository;
import com.hyunwoo.sprinkling.repository.TakeMoneyRepository;
import com.hyunwoo.sprinkling.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class SprinklingServiceTest {
    @Autowired
    private SprinklingRepository sprinklingRepository;
    @Autowired
    private TakeMoneyRepository takeMoneyRepository;

    private SprinklingService sprinklingService;

    private MoneySprinkling moneySprinkling;
    private Sprinkling sprinkling;
    @BeforeEach
    public void setting(){
        moneySprinkling = MoneySprinkling.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(5)
                .userId(4387529387645l)
                .build();
        sprinklingService = new SprinklingService(sprinklingRepository, takeMoneyRepository);
    }

    @Test
    void sprinklingService_saveTest(){
        String token = RandomUtil.getRandomString(3);
        System.out.println(token);
        this.sprinkling = new Sprinkling(token, moneySprinkling);
        sprinklingRepository.save(sprinkling);
        System.out.println(sprinkling.getId());
        assertNotNull(sprinkling.getId());
        assertEquals(sprinkling.getAmount(), moneySprinkling.getAmount());
        assertEquals(sprinkling.getRoomId(), moneySprinkling.getRoomId());
        assertEquals(sprinkling.getTargetCount(), moneySprinkling.getTargetCount());
        assertEquals(sprinkling.getOwnerId(), moneySprinkling.getUserId());
    }

    @Test
    void sprinklingService_divTest(){
        String token = RandomUtil.getRandomString(3);
        final Sprinkling sprinkling = new Sprinkling(token, moneySprinkling);
        List<TakeMoney> list = sprinklingService.divideMoney(sprinkling);
        assertEquals(list.size(), moneySprinkling.getTargetCount());
        AtomicReference<Long> amount = new AtomicReference<>(0l);
        list.forEach((takeMoney) -> {
            amount.updateAndGet(v -> v + takeMoney.getAmount());
        });
        assertEquals(amount.get(), moneySprinkling.getAmount());
    }

    @Test
    void sprinklingService_sprinklingInfo(){
        sprinklingService_saveTest();

        Sprinkling localSprinkling = sprinklingService.getSprinklingInfo(sprinkling.getOwnerId(), sprinkling.getRoomId(), sprinkling.getToken());

        assertEquals(localSprinkling, sprinkling);
    }

    @Test
    void sprinklingService_create(){
        String token = sprinklingService.sprinkling(moneySprinkling);
        System.out.println(token);
    }
}