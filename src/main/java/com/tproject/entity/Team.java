package com.tproject.entity;
import lombok.*;
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {
    private int id;
    private String teamName;
    private int teamLeadId;
}
