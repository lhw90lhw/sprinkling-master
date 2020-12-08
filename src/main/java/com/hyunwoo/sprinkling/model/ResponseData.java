package com.hyunwoo.sprinkling.model;

import com.hyunwoo.sprinkling.entity.Sprinkling;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseData {
    private String token;
    private String createdTime;
    private Long amount;
    private Long takeMoney;
    private List<TakeMoneyDetail> takeMoneyDetails;

    public ResponseData(String token){
        this.token = token;
    }

    public ResponseData(Long takeMoney){
        this.takeMoney = takeMoney;
    }

    public ResponseData(Sprinkling sprinklingInfo){
        this.amount = sprinklingInfo.getAmount();
        this.createdTime = sprinklingInfo.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.takeMoney = 0l;
        this.takeMoneyDetails = new ArrayList<TakeMoneyDetail>();
        sprinklingInfo.getTakeMoneyList().forEach((tm) -> {
            if(tm.getReceiveUserId() != null){
                this.takeMoney += tm.getAmount();
                takeMoneyDetails.add(new TakeMoneyDetail(tm.getAmount(), tm.getReceiveUserId()));
            }
        });
    }

}
