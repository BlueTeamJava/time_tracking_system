package com.tproject.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private int id;
    private int teamId;
    private String name;
}
