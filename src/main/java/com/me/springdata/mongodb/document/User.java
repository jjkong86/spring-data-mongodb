package com.me.springdata.mongodb.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document
public class User {
    @Id
    private String id;
    private Long userId;
    private String firstName;
    private String lastName;
    private int age;
    private String[] locList;
    @Field("ct")
    @JsonIgnore
    private LocalDateTime createTime;

    @Field("detail_id")
    @DocumentReference(lazy = true, lookup = "{ 'userId' : ?#{#target} }")
    private UserDetail userDetail;

    private Address address;
}