package com.me.springdata.mongodb.config.mongo;

import com.me.springdata.mongodb.document.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;


@Component
@AllArgsConstructor
@Log4j2
public class CreateMongoDBIndex {
    MongoTemplate mongoTemplate;
    public static final String ID_PROPERTY_NAME = "_id";

    @PostConstruct
    public void init() {
        log.info("start ==> create mongodb index.");
        Class<?> userClazz = User.class;
        String indexId = getIdField(userClazz);
        if (!indexId.equals("")) {
            mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("userId", Sort.Direction.DESC).unique());
        }

        log.info("end ==> create mongodb index.");
    }

    public String getIdField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.getName().equals(ID_PROPERTY_NAME)) {
                    return "";
                }

                return field.getName();
            }
        }

        throw new RuntimeException("not found id filed in collection class.");
    }
}
