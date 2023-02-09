package com.me.springdata.mongodb.config.mongo.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BeforeSaveListener<T> extends AbstractMongoEventListener<T> {
    @Override
    public void onBeforeSave(BeforeSaveEvent<T> event) {
//        log.info(String.format("onBeforeSave(%s, %s)", SerializationUtils.serializeToJsonSafely(event.getSource()), SerializationUtils.serializeToJsonSafely(event.getDocument())));
        super.onBeforeSave(event);
    }
}
