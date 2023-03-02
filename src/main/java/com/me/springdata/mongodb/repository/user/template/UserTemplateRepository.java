package com.me.springdata.mongodb.repository.user.template;

import com.me.springdata.mongodb.document.User;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserTemplateRepository {

    MongoTemplate mongoTemplate;


    public List<User> findAll() {
        return mongoTemplate.find(new Query(), User.class);
    }


    public User insert(User user) {
        return mongoTemplate.insert(user);
    }

    public User save(User user) {
        return mongoTemplate.save(user);
    }

    public UpdateResult upsert(Query query, Update update) {
        return mongoTemplate.upsert(query, update, User.class);
    }

    public User findByUserId(Long userId) {
        User resultOne = mongoTemplate.findOne(
                new Query().addCriteria(Criteria.where("userId").is(userId)), User.class);
        return Optional.ofNullable(resultOne)
                .orElseThrow(() -> new RuntimeException("not found user by userId."));
    }

    public CompletableFuture<List<User>> findByFirstNameOrderByFirstNameAsc(String firstName) {
        Query query = new Query().addCriteria(
                        Criteria.where("firstName").is(firstName))
                .with(Sort.by(Sort.Direction.ASC, "firstName"));
        query.fields().include("userId").include("firstName");

        return CompletableFuture.supplyAsync(() -> mongoTemplate.find(query, User.class));
    }

    public List<User> findTop3ByUserIdInOrderByFirstNameDescAgeAsc(List<Long> userIdList,
            int limit) {
        Query query = new Query().addCriteria(Criteria.where("userId").in(userIdList))
                .with(Sort.by(Sort.Direction.DESC, "firstName"))
                .with(Sort.by(Sort.Direction.ASC, "age"))
                .limit(limit);

        List<User> users = mongoTemplate.find(query, User.class);
        return Optional.of(users).orElseGet(ArrayList::new);
    }

    public long updateLocByUserId(Long userId, String[] locList) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update().set("locList", locList);
        return mongoTemplate.upsert(query, update, User.class).getModifiedCount();
    }

    public long deleteByUserId(Long userId) {
        return mongoTemplate.remove(new Query().addCriteria(Criteria.where("userId").is(userId)),
                User.class).getDeletedCount();
    }
}
