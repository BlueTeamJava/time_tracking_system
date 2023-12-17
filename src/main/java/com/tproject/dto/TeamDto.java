package com.tproject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamDto {
    int id;
    String teamName;
    int teamLeadId;

}
