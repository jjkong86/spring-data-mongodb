package com.me.springdata.mongodb.config.mongo.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BeforeConvertListener<T> extends AbstractMongoEventListener<T> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<T> event) {
//        log.info("onBeforeConvert({})", event.getSource());
        super.onBeforeConvert(event);
    }
}
