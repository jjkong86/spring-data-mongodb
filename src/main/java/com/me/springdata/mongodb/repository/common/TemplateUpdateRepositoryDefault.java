package com.me.springdata.mongodb.repository.common;

import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TemplateUpdateRepositoryDefault<T> implements TemplateUpdateRepository<T> {
    private final MongoTemplate mongoTemplate;

    @Override
    public <S extends T> UpdateResult update(Query query, Update update, Class<S> clazz) {
        return mongoTemplate.upsert(query, update, clazz);
    }
}
