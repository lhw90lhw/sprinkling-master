package com.hyunwoo.sprinkling.controller;

import com.hyunwoo.sprinkling.entity.Sprinkling;
import com.hyunwoo.sprinkling.util.GsonUtil;
import com.hyunwoo.sprinkling.model.ApiResponse;
import com.hyunwoo.sprinkling.model.MoneySprinkling;
import com.hyunwoo.sprinkling.model.ResponseData;
import com.hyunwoo.sprinkling.constants.HeaderField;
import com.hyunwoo.sprinkling.entity.TakeMoney;
import com.hyunwoo.sprinkling.service.SprinklingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MoneySprinklingController {

    private final SprinklingService sprinklingService;

    @PostMapping("/sprinkling")
    public ApiResponse moneySprinkling(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @RequestBody MoneySprinkling moneySprinkling) {
        moneySprinkling.setUserId(userId);
        moneySprinkling.setRoomId(roomId);

        ResponseData responseData = new ResponseData(sprinklingService.sprinkling(moneySprinkling));

        log.info("CREATE SUCCESS SPRINKLING TOKEN {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }

    @PutMapping("/take/{token}")
    public ApiResponse takeMoneyByToken(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @PathVariable("token") String token) {

        TakeMoney takeMoney = sprinklingService.takeMoneyByToken(userId, roomId, token);
        ResponseData responseData = new ResponseData(takeMoney.getAmount());

        log.info("TAKE MONEY {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }

    @GetMapping("/sprinkling/{token}")
    public ApiResponse sprinklingInfo(
            @RequestHeader(HeaderField.USER_ID) Long userId,
            @RequestHeader(HeaderField.ROOM_ID) String roomId,
            @PathVariable("token") String token) {

        Sprinkling sprinklingInfo = sprinklingService.getSprinklingInfo(userId, roomId, token);
        ResponseData responseData = new ResponseData(sprinklingInfo);

        log.info("SPRINKLING INFO {}", GsonUtil.toString(responseData));
        return new ApiResponse(responseData);
    }
}
