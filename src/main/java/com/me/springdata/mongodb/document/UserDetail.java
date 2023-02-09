package com.me.springdata.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@Document
public class UserDetail {
    @Id
    private String id;
    private Long userId;
    private String loc;
    private String hp;
    @Field("ct")
    private LocalDateTime createTime;
}