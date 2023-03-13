package com.me.springdata.mongodb.repository.user;

import com.me.springdata.mongodb.document.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends MongoRepository<User, Long> {

    User findByUserId(Long userId);
    @Query(fields = "{'userId' : 1, 'firstName' : 1}")
    CompletableFuture<List<User>> findByFirstNameOrderByFirstNameAsc(String firstName);
    @Query("{'loc_list': {'$elemMatch' : {'$eq' : ?0}}}")
    List<User> findByLocElemMatch(String loc);
    @Query("{'userId': ?0}")
    @Update("{ '$set' : { 'age' : ?1 } }")
    long updateByUserIdSetAge(Long userId, int age);
    long deleteByUserId(Long userId);


    List<User> findTop3ByUserIdInOrderByFirstNameDescAgeAsc(List<Long> userIdList);

    String queryDefault = "{}";

    String fieldsFindAllOrderByLastNameAsc = "{userId : 1, age: 1}";

    @Query(value = queryDefault, fields = fieldsFindAllOrderByLastNameAsc)
    CompletableFuture<List<User>> findAllOrderByLastNameAsc();

    Optional<Page<User>> findLimitByFirstName(String firstName, Pageable pageable);

    List<User> findTop3ByUserIdInOrderByFirstNameDescAgeAsc(List<Long> userIdList, String str);

    default List<User> findByUserIdList(List<Long> userIdList) {
        return findTop3ByUserIdInOrderByFirstNameDescAgeAsc(userIdList);
    }

    @Query("{'address.addressId': ?0}")
    Optional<List<User>> findByAddressId(Long addressId);
}