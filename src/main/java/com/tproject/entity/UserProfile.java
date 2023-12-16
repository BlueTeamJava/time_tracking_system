package com.tproject.entity;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private int id;
    private int teamId;
    private String name;
}
