package com.hyunwoo.sprinkling.controller;

import com.hyunwoo.sprinkling.constants.StatusCode;
import com.hyunwoo.sprinkling.exception.MoneySprinklingException;
import com.hyunwoo.sprinkling.model.ApiResponse;
import com.hyunwoo.sprinkling.model.MoneySprinkling;
import com.hyunwoo.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

        @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
        class MoneySprinklingControllerTest {
            @Autowired
            private SprinklingService sprinklingService;

            private MoneySprinkling moneySprinkling;
            private MoneySprinklingController moneySprinklingController;

            @BeforeEach
            void setUp() {
                moneySprinklingController = new MoneySprinklingController(sprinklingService);
    }

    @Test
    void moneySprinkling_success() {
        MoneySprinkling moneySprinkling = MoneySprinkling.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(5)
                .userId(4387529387645l)
                .build();
        ApiResponse apiResponse = moneySprinklingController.moneySprinkling(472876l, "_rHusq", moneySprinkling);
        assertEquals(apiResponse.getDescription(), "Success");
    }

    @Test
    void moneySprinkling_fail() {
        MoneySprinkling moneySprinkling = MoneySprinkling.builder()
                .amount(10000)
                .roomId("_rH6sn")
                .targetCount(0)
                .userId(4387529387645l)
                .build();
        try{
            ApiResponse apiResponse = moneySprinklingController.moneySprinkling(472876l, "_rHusq", moneySprinkling);
        }catch(MoneySprinklingException e){
            assertEquals(e.getResultCode(), StatusCode.E400.getCode());
        }
    }

    @Test
    void moneySprinkling_fail2() {
        MoneySprinkling moneySprinkling = MoneySprinkling.builder()
                .amount(100)
                .roomId("_rH6sn")
                .targetCount(101)
                .userId(4387529387645l)
                .build();
        try{
            ApiResponse apiResponse = moneySprinklingController.moneySprinkling(472876l, "_rHusq", moneySprinkling);
        }catch(MoneySprinklingException e){
            assertEquals(e.getResultCode(), StatusCode.E406.getCode());
        }
    }

    @Test
    void takeMoneyByToken_fail() {
        try{
            ApiResponse apiResponse = moneySprinklingController.takeMoneyByToken(472876l, "_rHusq", "Hwk");
        }catch(MoneySprinklingException e){
            assertEquals(e.getResultCode(), StatusCode.E401.getCode());
        }
    }

    @Test
    void sprinklingInfo() {
        try{
            ApiResponse apiResponse = moneySprinklingController.sprinklingInfo(472876l, "_rHusq", "Hwk");
        }catch(MoneySprinklingException e){
            assertEquals(e.getResultCode(), StatusCode.E405.getCode());
        }
    }
}