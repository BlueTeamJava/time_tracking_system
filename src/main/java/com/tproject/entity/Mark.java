package com.tproject.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Mark {
    private int id;
    private int userProfileId;
    private float score;
    private Date date;
}
