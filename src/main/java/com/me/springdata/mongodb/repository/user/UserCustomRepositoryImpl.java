package com.me.springdata.mongodb.repository.user;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.repository.common.TemplateUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
    MongoTemplate mongoTemplate;
    TemplateUpdateRepository<User> templateUpdateRepository;

    @Override
    public User findTemplateByUserId(Long userId) {
        return mongoTemplate.findOne(new Query().addCriteria(Criteria.where("userId").is(userId)), User.class);
    }

    @Override
    public long updateLocByUserId(Long userId, String loc) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update().addToSet("locList", loc);
        return templateUpdateRepository.update(query, update, User.class).getModifiedCount();
    }
}