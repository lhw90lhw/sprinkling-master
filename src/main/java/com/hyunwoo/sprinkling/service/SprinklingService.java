package com.hyunwoo.sprinkling.service;

import com.hyunwoo.sprinkling.constants.StatusCode;
import com.hyunwoo.sprinkling.entity.Sprinkling;
import com.hyunwoo.sprinkling.exception.MoneySprinklingException;
import com.hyunwoo.sprinkling.repository.SprinklingRepository;
import com.hyunwoo.sprinkling.repository.TakeMoneyRepository;
import com.hyunwoo.sprinkling.model.MoneySprinkling;
import com.hyunwoo.sprinkling.entity.TakeMoney;
import com.hyunwoo.sprinkling.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SprinklingService {
    private final SprinklingRepository sprinklingRepository;
    private final TakeMoneyRepository takeMoneyRepository;

    public Sprinkling getSprinklingInfo(Long userId, String roomId, String token) throws MoneySprinklingException {
        LocalDateTime time = LocalDateTime.now();
        time = time.minusDays(7);
        Optional<Sprinkling> sprinkling = sprinklingRepository.findByTokenAndRoomIdAndOwnerIdAndCreatedTimeAfter(
                token, roomId, userId, time);
        sprinkling.orElseThrow(()->new MoneySprinklingException(StatusCode.E405));
        return sprinkling.get();
    }

    @Transactional
    public String sprinkling(MoneySprinkling moneySprinkling) throws MoneySprinklingException{

        if(moneySprinkling.getTargetCount() < 1 || moneySprinkling.getAmount() < 1){
            throw new MoneySprinklingException(StatusCode.E400);
        }

        if(moneySprinkling.getTargetCount() > moneySprinkling.getAmount()){
            throw new MoneySprinklingException(StatusCode.E406);
        }

        String token = createToken(moneySprinkling);
        Sprinkling p = new Sprinkling(token, moneySprinkling);
        p = sprinklingRepository.save(p);

        takeMoneyRepository.saveAll(divideMoney(p));

        return token;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TakeMoney takeMoneyByToken(Long userId, String roomId, String token) throws MoneySprinklingException {

        LocalDateTime time = LocalDateTime.now();
        time = time.minusMinutes(10);
        Optional<Sprinkling> sprinkling = sprinklingRepository.findByTokenAndRoomIdAndCreatedTimeAfter(token, roomId, time);
        sprinkling.orElseThrow(()->new MoneySprinklingException(StatusCode.E401));

        if(sprinkling.get().getOwnerId().equals(userId)){
            throw new MoneySprinklingException(StatusCode.E402);
        }

        log.info("TOKEN {}, AMOUT {}, TARGETCOUNT {}",
                sprinkling.get().getToken(), sprinkling.get().getAmount(), sprinkling.get().getTargetCount());

        TakeMoney takeMoney = null;
        for(TakeMoney money : sprinkling.get().getTakeMoneyList()) {
            if(money.getReceiveUserId() != null && money.getReceiveUserId().equals(userId)){
                throw new MoneySprinklingException(StatusCode.E403);
            }else if(money.getReceiveUserId() == null && takeMoney == null) {
                takeMoney = money;
            }
        }

        if(takeMoney == null){
            throw new MoneySprinklingException(StatusCode.E404);
        }

        takeMoney.take(userId);
        log.info("takemoney get {} : {} : {}", takeMoney.getId(), takeMoney.getAmount(), takeMoney.getReceiveUserId());
        return takeMoney;
    }

    public List<TakeMoney> divideMoney(Sprinkling sprinkling){

        List<TakeMoney> divs = new ArrayList<TakeMoney>();
        Long amount = sprinkling.getAmount() - sprinkling.getTargetCount();
        Long divided = 0l;

        for(long i = 0; i< sprinkling.getTargetCount()-1; i++){
            Long plusMoney = 1l;
            if(amount-divided > 2){
                plusMoney += RandomUtil.getRandLong(amount-divided);
            }
            log.info("div money {}", plusMoney);
            divs.add(new TakeMoney(sprinkling, null, plusMoney));
            divided += plusMoney;
        }
        log.info("div money {}", sprinkling.getAmount() - divided);
        divs.add(new TakeMoney(sprinkling, null, sprinkling.getAmount() - divided));

        return divs;
    }

    public String createToken(MoneySprinkling moneySprinkling){

        String token = RandomUtil.getRandomString(3);
        LocalDateTime time = LocalDateTime.now();
        time.minusMinutes(10);
        while(sprinklingRepository.countByTokenAndOwnerIdAndRoomIdAndCreatedTimeAfter(
                token, moneySprinkling.getUserId(), moneySprinkling.getRoomId(), time) > 0){
            token = RandomUtil.getRandomString(3);
        }
        return token;
    }
}
