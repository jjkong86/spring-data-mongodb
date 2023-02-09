package com.me.springdata.mongodb.repository.common;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface TemplateUpdateRepository<T> {
    <S extends T> UpdateResult update(Query query, Update update, Class<S> clazz);
}
