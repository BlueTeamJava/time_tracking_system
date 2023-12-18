package com.tproject.dto;

import com.tproject.entity.Roles;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private int id;
    private String username;
    private String password;
    private String name;
    private Roles role;
}
