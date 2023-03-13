## Spring Data MongoDB

### Config

### 설정(Domain Class, Repository)

## Optimistic Locking

## DocumentReference란

---

1. DocumentReference는 Entity간 참조를 허용하여 유연한 스키마를 사용할 수 있게 합니다. DBRef를 사용할 때와 목표는 같지만 표현방식이 다릅니다.
2. One-to-One, Many-to-one 및 Many-to-many와 같은 모든 일반적인 유형이 지원됩니다. <br>
    + 양방향 연관관계의 경우 @ReadOnlyProperty가 필요함
    + cascade는 지원되지 않음
3. @DocumentReference는는 3.3 version 부터 사용할 수 있습니다.

## DocumentReference 사용 방법

---

1. Lazy Option
    + @DocumentReference에서 `lazy = true` 설정을 권장합니다.<br>

```
    @Field("detail_id")
    @DocumentReference(lazy = true, lookup = "{ 'detailId' : ?#{#target} }")
    private UserDetail userDetail;
```

2. Data 형태

    + User Collection의 `detail_id` Field는 느슨한 연관관계의 형태로 `userDetail.detail_id` 참조값을 갖고 있습니다.

```
{ 
    "_id" : ObjectId("640ad00528082669a6cb37e1"), 
    "userId" : NumberLong(1), 
    "firstName" : "Jeong", 
    "age" : NumberInt(18), 
    "loc_list" : [
        "seoul", 
        "busan"
    ], 
    "ct" : ISODate("2023-03-10T06:36:53.765+0000"), 
    "detail_id" : NumberLong(1), // userDetail.detail_id
    "version" : NumberLong(0)
}
```

3. Lazy Test
    + MongoDB의 Find Query를 확인하려면 `db.setLogLevel(1)`으로 Log Level 설정이 필요합니다.
    + Find Query Log를 확인해보면 Test Code의 `Thread.sleep(1000)` 때문에 1초 후에 연관관계 Find Query가 실행 되었습니다.

```
    @Test
    public void user_reference_lazy_test() throws InterruptedException {
        //given
        long userId = 1L;
        //when
        User user = userRepository.findByUserId(userId);

        //done
        Thread.sleep(1000);
        log.info(user.getUserDetail()); // lazy option으로 참조 시점에 find query 실행
    }
    
   - MongoDB Find Query Log
   2023-03-10T15:36:53.835+0900 I  COMMAND  [conn54] command user command: find { find: "user", filter: { userId: 1 } ...
   2023-03-10T15:36:54.865+0900 I  COMMAND  [conn54] command userDetail command: find { find: "userDetail", filter: { userId: 1 } ...
```

4. N+1 문제
    + Spring Data MongoDB 에서는 Jpql의 Fetch Join과 같은 해결방법이 없습니다.
    + 명시적으로 Field에 데이터를 주입해주는 방법으로 해결할 수 있습니다.

```
    @Test
    public void list_user_reference_field_injection_test() {
        //when
        List<User> findAllUser = userRepository.findAll();
        
        // get userDetailList by userIDList
        List<Long> userIdList = findAllUser.stream().map(User::getUserId).toList();
        List<UserDetail> findUserDetailList = userDetailRepository.findByUserIdIn(userIdList);
        Map<Long, UserDetail> userDetailMap = findUserDetailList.stream().collect(Collectors.toMap(UserDetail::getUserId, Function.identity()));
        
        findAllUser.forEach(user -> user.setUserDetail(userDetailMap.get(user.getUserId()))); // field에 data 설정

        //done
        findAllUser.forEach(user -> log.info(user.getUserDetail()));
        
    }
    
   - MongoDb Find Query Log
   2023-03-10T17:02:24.943+0900 I  COMMAND  [conn73] command user command: find { find: "user", filter: {}, ...
   2023-03-10T17:02:24.975+0900 I  COMMAND  [conn73] command userDetail command: find { find: "userDetail", filter: { userId: { $in: [ 1, 2, 4, 3 ] } } ...
```