현재 개발하고 있는 서비스에서 메인디비로 MongoDB를 사용하고 있고, 제어는 MongoTemplate을 이용하고 있습니다.
MongoTemplate을 사용하면서 불편한점과 문제점들이 있었습니다. <br>

1. 쿼리 작성하는데 적지 않은 시간을 사용하고 있음
2. 쿼리의 오류를 컴파일 타임에 알지 못함
3. 객체 중심 개발이 힘듬

그래서 Spring Data MongoDB를 도입하기 위해 R&D를 진행하였고, 그 과정에서 얻은 내용을 공유 드리겠습니다.

## Spring Data MongoDB란

---
Spring Data MongoDB는 스프링 프레임워크와 MongoDB 데이터베이스를 통합하는 기술로 `Repository 스타일`의 데이터 엑세스 레이어를 빠르고 간단하게<br>
구현할 수 있도록 도와주며, 코드의 가독성와 유지보수성을 높히고 MongoDB를 보다 효율적으로 활용할 수 있도록 도와주는 기능을 제공합니다.<br>

### Config

---
Spring Boot Version `2.7.7`, `Java 17`을 사용하고, `spring-boot-starter-data-mongodb`를 의존성에 추가해줍니다.

### Domain Class 설정

---
Repository를 사용하기 위해서 MongoDB의 컬렉션과 Mapping되는 `Domain Class`와 추상화의 중심 인터페이스인 `Repository`를 설정해야 합니다.<br>
먼저 Domain Class 설정 방법에 대해 알아보겠습니다.<br>
Domain Class에 `@Document` 사용하여 지정해줍니다. 별도의 Collection Name을 설정하지 않으면 기본으로 Class Name(camelcase)을 컬렉션명으로 사용합니다.<br>
`@Id`로 Collection의 기본키를 지정해주고, 필요한 경우 `@Field`로 필드명을 설정합니다. <br>

```java

@Document(collection = "User")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long userId;
    private String firstName;
    private String lastName;
    private int age;
    @Field("loc_list")
    private String[] locList;
    @Field("ct")
    @CreatedDate
    private LocalDateTime createTime;

    @Field("detail_id")
    @DocumentReference(lazy = true, lookup = "{ 'detailId' : ?#{#target} }")
    private UserDetail userDetail;

    @Version
    Long version;
}

@Document(collection = "UserDetail")
public class UserDetail {

    @Id
    private String id;
    @Field("detail_id")
    private Long detailId;
    private String loc;
    private String hp;
    @Field("ct")
    private LocalDateTime createTime;

    @Version
    Long version;
}
```

## Repository 설정

---
다음으로 Repository 설정 방법에 대해 알아보겠습니다.<br>

1. Repository는 `MongoRepository`, `PagingAndSortingRepository`, `CrudRepository`를 목적에 맞게 선택하고, Generic Type으로 `Collection` Type과 Collection의 `_id`
   Field Type을 설정합니다.
    + `PagingAndSortingRepository`의 paging 기능을 이용하면, skip 명령어가 실행되어 offset이 증가함에 따라 성능저하 현상이 발생할 수 있기 때문에 주의해서 사용 해야함

2. Default로 기본적인 단건과 다건의 CRUD Method는 정의가 되어있고, Method 생성 규칙에 맞게 선언해서 사용하면 됩니다. 또한 사용자 지정 Method도 가능합니다.
    + [Defining Query Methods](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories.query-methods.details)
3. @Query 사용
    + 특정 필드만 반환해야 한다면 `fields` option을 고려(=Projection)
    + 배열 Field의 조회는 `$elemMatch`를 사용해야함

4. @Update를 이용하여 update문을 수행할 수 있습니다.
    + Repository에서 save를 지원함
        + 컬렉션에서 `_id` 기준으로 일치하는 Document가 존재하면 update가 되고, 없으면 Insert됨
        + 지원되는 method : save(), saveAll()
    + 사용자 지정 method에는 동작하지 않기 때문에 `@Query`와 함께 사용하는것이 좋음
        + update 동작은 update명칭이 들어가는것이 명확하기 때문, 아래 두 Method의 동작은 같음
          ```java
          @Query("{'userId': ?0}")
          @Update("{ '$set' : { 'age' : ?1 } }")
          long updateByUserIdSetAge(Long userId, int age); // 사용자 지정 method
          
          @Update("{ '$set' : { 'age' : ?1 } }")
          long findByUserIdSetAge(Long userId, int age);
          ```
    + Collection의 Filed가 많거나 특정 Field의 Size가 크다면, @Update 사용을 고려 하면 최적화 가능
    + [Supported Query Return Types](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#appendix.query.return.types)

```java
public interface UserRepository extends MongoRepository<User, Long> {

    User findByUserId(Long userId);

    @Query(fields = "{'userId' : 1, 'firstName' : 1}")
    CompletableFuture<List<User>> findByFirstNameOrderByFirstNameAsc(String firstName);

    @Query("{'loc_list': {'$elemMatch' : {'$eq' : ?0}}}")
    List<User> findByLocElemMatch(String loc);

    @Query("{'userId': ?0}")
    @Update("{ '$set' : { 'age' : ?1 } }")
    long updateByUserIdSetAge(Long userId, int age); // 사용자 지정 method

    void deleteByUserId(Long userId);
}
```

<br>
이제 Service Layer에서 MongoDB의 Document 데이터 조회나 수정을 위해 만들어 둔 Repository 인터페이스를 사용할 수 있게됩니다.
<br>

```java
public class UserService {

    public UserResponse findUserByUserId(Long userId) {
        return UserResponse.builder().user(userRepository.findByUserId(userId)).build();
    }
}
```

## References

---
[1] https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/ <br>
[2] https://www.concretepage.com/spring-5/spring-data-mongodb-query <br>
[3] https://medium.com/@andris.briedis/optimistic-locking-in-mongo-69fa693864ec <br>
