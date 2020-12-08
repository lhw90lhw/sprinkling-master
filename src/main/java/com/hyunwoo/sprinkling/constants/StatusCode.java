package com.hyunwoo.sprinkling.constants;

public enum StatusCode {
    E401(401, "이미 만료되었거나 존재하지 않는 받기 입니다."),
    E402(402, "본인이 뿌리기 한 금액은 받을 수 없습니다."),
    E403(403, "이미 받았습니다."),
    E404(404, "선착순 받기가 모두 끝났습니다."),
    E405(405, "요청하신 토큰이 7일이 경과 하였거나, 유효하지 않은 토큰 입니다."),
    E406(406, "금액이 인원수 보다 작습니다."),

    E400(400, "Bad Request"),
    E500(500, "Internal Server Error"),
    ;

    private int code;
    private String description;

    StatusCode(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode(){
        return code;
    }
    public String getDescription(){
        return description;
    }
}
