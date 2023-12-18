package com.tproject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamDto {
    private int id;
    private String teamName;
    private int teamLeadId;

}
