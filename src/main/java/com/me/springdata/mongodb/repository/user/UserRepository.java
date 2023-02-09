package com.me.springdata.mongodb.repository.user;

import com.me.springdata.mongodb.document.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends MongoRepository<User, Long>, UserCustomRepository {
    String queryDefault = "{}";
    String fieldsFindAllOrderByLastNameAsc = "{userId : 1, age: 1}";
//    ========================

    long deleteByUserId(Long UserId);

    @Query(value = queryDefault, fields = fieldsFindAllOrderByLastNameAsc)
    CompletableFuture<List<User>> findAllOrderByLastNameAsc();

    @Query("{ 'firstName' : ?0 }")
    List<User> findUsers(String FistName);

    @Query(fields = "{userId : 1, firstName : 1}")
    CompletableFuture<List<User>> findByFirstNameOrderByFirstNameAsc(String firstName);

    Optional<Page<User>> findLimitByFirstName(String firstName, Pageable pageable);

    User findByUserId(Long userId);

    List<User> findTop3ByUserIdInOrderByFirstNameDescAgeAsc(List<Long> userIdList);
    List<User> findTop3ByUserIdInOrderByFirstNameDescAgeAsc(List<Long> userIdList, String str);

    default List<User> findByUserIdList(List<Long> userIdList) {
        return findTop3ByUserIdInOrderByFirstNameDescAgeAsc(userIdList);
    }

    @Query("{'address.addressId': ?0}")
    Optional<List<User>> findByAddressId(Long addressId);
}