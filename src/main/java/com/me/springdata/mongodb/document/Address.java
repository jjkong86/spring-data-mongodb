package com.me.springdata.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@Document
public class Address {
    @Id
    private String id;
    private Long addressId;
    private String loc;
}
