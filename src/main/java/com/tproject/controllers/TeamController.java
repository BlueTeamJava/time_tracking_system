package com.tproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tproject.annotations.Controller;
import com.tproject.annotations.HttpMethod;
import com.tproject.annotations.RequestMapping;
import com.tproject.dto.TeamDto;
import com.tproject.exception.CustomSQLException;
import com.tproject.services.impl.JWTServiceImpl;
import com.tproject.services.impl.TeamServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Controller
public class TeamController {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final TeamServiceImpl teamService = TeamServiceImpl.getInstance();

    private HttpServletResponse sendError(int errorCode, String errorReason, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(errorCode);
        PrintWriter out = resp.getWriter();
        out.println(errorReason);
        return resp;
    }

    private boolean validateTeamDto(TeamDto teamDto) {

        return teamDto != null && teamDto.getId()!=0 && !teamDto.getTeamName().isEmpty();
    }

    @RequestMapping(url="/teams",method = HttpMethod.GET)
    public HttpServletResponse getAllTeams(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Collection<TeamDto> teams = teamService.getAllTeams();

            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            if (teams.isEmpty()) {
                out.println("[]");
            } else {
                out.println(jsonMapper.writeValueAsString(teams));
            }

            return res;

        }catch (JwtException e){
            return sendError(401,"Unauthorized user",res);
        }

    }
    @RequestMapping(url="/teams",method = HttpMethod.DELETE)
    public HttpServletResponse deleteTeam(HttpServletRequest req, HttpServletResponse res){
        int teamId = Integer.parseInt(req.getParameter("id"));
       if(teamService.deleteTeam(teamId)){
           res.setStatus(204);
           return res;
       }else{
           res.setStatus(500);
           return res;
       }
    }
    @RequestMapping(url="/teams",method = HttpMethod.POST)
    public HttpServletResponse createTeam(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String requestData = req.getReader().lines().reduce("", String::concat);
        ObjectMapper jsonMapper = new ObjectMapper();
        TeamDto teamDto = jsonMapper.readValue(requestData, TeamDto.class);

        System.out.println(teamDto.getTeamName());
        if(teamService.createTeam(teamDto)){
            res.setStatus(204);
            return res;
        }else{
            res.setStatus(500);
            return res;
        }

    }

    @RequestMapping(url = "/teams", method = HttpMethod.PUT)
    public HttpServletResponse updateTeam(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String teamId = req.getParameter("id");

            if (teamId != null) {
                String teamData = req.getReader().lines().reduce("", String::concat);
                TeamDto updatedTeam = jsonMapper.readValue(teamData, TeamDto.class);
                if (validateTeamDto(updatedTeam)) {
                    teamService.updateTeam(updatedTeam);
                    resp.setStatus(200);
                } else {
                    return sendError(400, "Invalid team data", resp);
                }
                return resp;
            } else {
                return sendError(400, "Team ID not specified", resp);
            }
        } catch (CustomSQLException e) {
            return sendError(500, e.getMessage(), resp);
        }
    }

}
