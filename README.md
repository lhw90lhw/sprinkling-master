# 카카오페이 뿌리기 API 서버 개발
   ## 프로젝트 구성
   * Java 1.8
   * Spring Boot
   * JPA
   * H2 Embedded
   
   ## 핵심 문제 해결 전략
   1. 뿌리기
      * 뿌리기 요청건에 대한 고유한 token 을 발급 (3자리)
      * token 생성시 유효성 검사 -> DB조회 (token, roomId, userId, 최근 10분간 생성된 토큰에 대한 검사)

   2. 받기
      * 할당되지 않은 분배건 하나를 선택 하여 할당
      * 동시성 문제 : Transaction Isolation Level REPEATABLE_READ 로 설정
      * 중복 받기 처리는 db uniquekey 로 처리