package com.me.springdata.mongodb.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
    @Indexed(unique = true)
    private Long userId;
    private String firstName;
    private String lastName;
    private int age;
    @Field("loc_list")
    private String[] locList;
    @Field("ct")
    @CreatedDate
    private LocalDateTime createTime;

    @Field("detail_id")
    @DocumentReference(lazy = true, lookup = "{ 'userId' : ?#{#target} }")
    private UserDetail userDetail;

    private Address address;
}