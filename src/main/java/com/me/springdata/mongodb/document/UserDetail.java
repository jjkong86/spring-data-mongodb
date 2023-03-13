package com.me.springdata.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@Document("UserDetail")
public class UserDetail {
    @Id
    private String id;
    @Field("detail_id")
    private Long detailId;
    private String loc;
    private String hp;
    @Field("ct")
    private LocalDateTime createTime;
    @Version
    Long version;
}