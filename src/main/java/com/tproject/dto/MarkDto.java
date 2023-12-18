package com.tproject.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MarkDto {
    private int id;
    private int userProfileId;
    private float score;
    private Date date;
    private String description;
}
