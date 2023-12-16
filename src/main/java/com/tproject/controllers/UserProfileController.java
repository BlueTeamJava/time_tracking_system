package com.tproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tproject.annotations.HttpMethod;
import com.tproject.annotations.RequestMapping;
import com.tproject.dto.UserDto;
import com.tproject.dto.UserProfileDto;
import com.tproject.exception.CustomSQLException;
import com.tproject.services.impl.UserProfileServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class UserProfileController {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final UserProfileServiceImpl userProfileService = UserProfileServiceImpl.getInstance();

    private HttpServletResponse sendError(int errorCode, String errorReason, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(errorCode);
        PrintWriter out = resp.getWriter();
        out.println(errorReason);
        return resp;
    }


    @RequestMapping(url = "/profile", method = HttpMethod.GET)
    public HttpServletResponse getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

                Optional<UserProfileDto> userProfileOpt = Optional.of(userProfileService.getUserProfile(Integer.parseInt(req.getParameter("id"))));

                if (userProfileOpt.isPresent()) {
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print(jsonMapper.writeValueAsString(userProfileOpt.get()));
                    return resp;
                } else {
                    return sendError(404, "Profile not found", resp);
                }


        } catch (CustomSQLException e) {
            return sendError(500, e.getMessage(), resp);
        }
    }

    @RequestMapping(url = "/profile", method = HttpMethod.POST)
    public HttpServletResponse saveUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserProfileDto creatingUserProfileDto = jsonMapper.readValue(req.getReader(), UserProfileDto.class);
        try {
            Optional<Integer> result = userProfileService.createUserProfile(creatingUserProfileDto);
            resp.setContentType("application/json");
            if (result != null) {
                resp.setStatus(201);
                return resp;
            } else {
                //not sure code 500 is OK here
                return sendError(500, "Something went wrong", resp);
            }
        } catch (CustomSQLException e) {
            return sendError(500, e.getMessage(), resp);
        }

    }

    @RequestMapping(url = "/profile", method = HttpMethod.PUT)
    public HttpServletResponse updateUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {

            UserProfileDto updatingUserProfileDto = jsonMapper.readValue(req.getReader(), UserProfileDto.class);

            if (userProfileService.getUserProfile(Integer.parseInt(req.getParameter("id"))) != null) {
                userProfileService.updateUserProfile(updatingUserProfileDto);
                return resp;
            } else {
                return sendError(404, "No profile found to update", resp);
            }
        } else {
            return sendError(400, "Profile id not specified", resp);
        }
    }


    @RequestMapping(url = "/profile", method = HttpMethod.DELETE)
    public HttpServletResponse deleteUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {
            if (userProfileService.deleteUserProfile(Integer.parseInt(req.getParameter("id")))) {
                return resp;
            } else {
                return sendError(404, "Profile not found", resp);
            }
        } else {
            return sendError(400, "Profile id not specified", resp);
        }
    }
}
