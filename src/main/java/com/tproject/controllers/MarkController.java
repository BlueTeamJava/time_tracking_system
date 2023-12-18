package com.tproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tproject.annotations.Controller;
import com.tproject.annotations.HttpMethod;
import com.tproject.annotations.RequestMapping;
import com.tproject.dto.MarkDto;
import com.tproject.exception.CustomSQLException;
import com.tproject.services.impl.MarkServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Controller
public class MarkController {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final MarkServiceImpl markService = MarkServiceImpl.getInstance();

    private HttpServletResponse sendError(int errorCode, String errorReason, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(errorCode);
        PrintWriter out = resp.getWriter();
        out.println(errorReason);
        return resp;
    }

    @RequestMapping(url = "/mark", method = HttpMethod.GET)
    public HttpServletResponse getMark(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            //get mark by user & date
            if (req.getParameter("userId") != null && req.getParameter("date") != null) {

                //date cast
                String dateString = req.getParameter("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateString);

                Optional<MarkDto> markOpt = Optional.of(markService.getMarkByUserDate(Integer.parseInt(req.getParameter("userId")), date));

                if (markOpt.isPresent()) {
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print(jsonMapper.writeValueAsString(markOpt.get()));
                    return resp;
                } else {
                    return sendError(404, "Mark not found", resp);
                }
            }
            //get all user marks
            else if (req.getParameter("userId") != null && req.getParameter("date") == null){

                Collection<MarkDto> markOpt = markService.getAllUserMarks(Integer.parseInt(req.getParameter("userId")));

                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print(jsonMapper.writeValueAsString(markOpt));
                    return resp;
            }
            //get mark by id
            else if (req.getParameter("id") != null) {

                Optional<MarkDto> markOpt = Optional.of(markService.getMarkById(Integer.parseInt(req.getParameter("id"))));

                if (markOpt.isPresent()) {
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print(jsonMapper.writeValueAsString(markOpt.get()));
                    return resp;
                } else {
                    return sendError(404, "Mark not found", resp);
                }

            } else if (req.getParameter("date") != null) {
                String dateString = req.getParameter("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateString);

                Collection<MarkDto> marksByDate = markService.getMarkByDate(date);

                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print(jsonMapper.writeValueAsString(marksByDate));
                return resp;
            }
        } catch (CustomSQLException e) {
            return sendError(500, e.getMessage(), resp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    @RequestMapping(url = "/mark", method = HttpMethod.POST)
    public HttpServletResponse createMark(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MarkDto creatingMarkDto = jsonMapper.readValue(req.getReader(), MarkDto.class);
        System.out.println(creatingMarkDto);
        try {
            Optional<Integer> result = markService.createMark(creatingMarkDto);
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

    @RequestMapping(url = "/totalmarks", method = HttpMethod.GET)
    public HttpServletResponse getTotalMarks(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<Integer, Integer> totalMarksByUsers = (markService.getTotalMarksByUsers());

                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print(jsonMapper.writeValueAsString(totalMarksByUsers));
                return resp;

        } catch (CustomSQLException e) {
            return sendError(500, e.getMessage(), resp);
        }
    }

    @RequestMapping(url = "/mark", method = HttpMethod.PUT)
    public HttpServletResponse updateMark(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {

            MarkDto updatingMarkDto = jsonMapper.readValue(req.getReader(), MarkDto.class);

            if (markService.getMarkById(Integer.parseInt(req.getParameter("id"))) != null) {
                markService.updateMark(updatingMarkDto);
                return resp;
            } else {
                return sendError(404, "No mark found to update", resp);
            }
        } else {
            return sendError(400, "Mark id not specified", resp);
        }
    }

    @RequestMapping(url = "/mark", method = HttpMethod.DELETE)
    public HttpServletResponse deleteMark(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {
            if (markService.deleteMark(Integer.parseInt(req.getParameter("id")))) {
                return resp;
            } else {
                return sendError(404, "Mark not found", resp);
            }
        } else {
            return sendError(400, "Mark id not specified", resp);
        }
    }
}
