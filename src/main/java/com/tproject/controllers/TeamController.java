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
import java.sql.SQLException;
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


    @RequestMapping(url="/teamslist",method = HttpMethod.GET)
    public HttpServletResponse getAllTeams(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Jws<Claims> jws = JWTServiceImpl.getInstance().verifyUserToken(req.getHeader("Authorization").replace("Bearer ", ""));
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
    @RequestMapping(url="",method = HttpMethod.DELETE)
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
    @RequestMapping(url="",method = HttpMethod.POST)
    public HttpServletResponse createTeam(HttpServletRequest req, HttpServletResponse res){
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamName(req.getParameter("teamName"));

        if(teamService.createTeam(teamDto)){
            res.setStatus(204);
            return res;
        }else{
            res.setStatus(500);
            return res;
        }

    }




}
