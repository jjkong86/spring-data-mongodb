package com.me.springdata.mongodb.config.mongo;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.util.TypeInformation;

import java.util.Set;

public class CustomMongoTypeMapper implements MongoTypeMapper {
    /**
     * Returns whether the given key is the type key.
     *
     * @param key
     * @return
     */
    @Override
    public boolean isTypeKey(String key) {
        return false;
    }

    /**
     * Writes type restrictions to the given {@link Document}. This usually results in an {@code $in}-clause to be
     * generated that restricts the type-key (e.g. {@code _class}) to be in the set of type aliases for the given
     * {@code restrictedTypes}.
     *
     * @param result          must not be {@literal null}
     * @param restrictedTypes must not be {@literal null}
     */
    @Override
    public void writeTypeRestrictions(Document result, Set<Class<?>> restrictedTypes) {

    }

    /**
     * Reads the {@link TypeInformation} from the given source.
     *
     * @param source must not be {@literal null}.
     * @return
     */
    @Override
    public TypeInformation<?> readType(Bson source) {
        return null;
    }

    /**
     * Returns the {@link TypeInformation} from the given source if it is a more concrete type than the given default one.
     *
     * @param source      must not be {@literal null}.
     * @param defaultType must not be {@literal null}.
     * @return
     */
    @Override
    public <T> TypeInformation<? extends T> readType(Bson source, TypeInformation<T> defaultType) {
        return null;
    }

    /**
     * Writes type information for the given type into the given sink.
     *
     * @param type     must not be {@literal null}.
     * @param dbObject must not be {@literal null}.
     */
    @Override
    public void writeType(Class<?> type, Bson dbObject) {

    }

    /**
     * Writes type information for the given {@link TypeInformation} into the given sink.
     *
     * @param type     must not be {@literal null}.
     * @param dbObject must not be {@literal null}.
     */
    @Override
    public void writeType(TypeInformation<?> type, Bson dbObject) {

    }
}
