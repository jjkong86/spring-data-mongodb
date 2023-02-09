package com.me.springdata.mongodb.config.mongo.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class OnAfterLoadListener<T> extends AbstractMongoEventListener<T> {
    @Override
    public void onAfterLoad(AfterLoadEvent<T> event) {
//        log.info("onAfterLoad({})", event.getDocument());
        super.onAfterLoad(event);
    }
}
