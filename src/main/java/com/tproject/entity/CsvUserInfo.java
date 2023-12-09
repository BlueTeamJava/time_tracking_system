package com.tproject.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CsvUserInfo {
    private int id;
    private String username;
    private List<Task> userTaskList;
}
