* 실무에서는
* 재시도가 필요하지 않는 lock 은 lettuce 활용
* 재시도가 필요한 경우에는 redisson 를 활용

[lettuce]
* 구현이 간단
* spring redis 를 쓰면 lettuce 가 기본임
* spin lock 방식이라서 동시에 많은 스레드가 lock 획득 대기 상태라면 redis 에 부하가 감

[redisson]
* 락 획득 재시도가 기본
* 펍 섭 방식으로 구현되어있어서 redis 에 비교적 부하가 덜감
* 별도 라이브러리 필요
* lock 을 라이브러리에서 제공해줘서 공부 후에 사용해야함

[mysql]
* redis 보다 lock 처리 성능이 좋지 않음