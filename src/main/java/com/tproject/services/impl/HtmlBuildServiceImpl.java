package com.tproject.services.impl;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.tproject.dao.impl.TaskDaoImpl;
import com.tproject.dao.impl.UserDaoImpl;
import com.tproject.entity.CsvUserInfo;
import com.tproject.entity.Task;
import com.tproject.entity.User;
import com.tproject.services.HtmlBuildService;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlBuildServiceImpl implements HtmlBuildService{

    private static HtmlBuildServiceImpl instance;

    private UserDaoImpl userDao = UserDaoImpl.getInstance();

    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();

    public static HtmlBuildServiceImpl getInstance() {
        HtmlBuildServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (HtmlBuildServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HtmlBuildServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public byte[] createHtmlFile() {
        try (StringWriter writer = new StringWriter()) {
            List<CsvUserInfo> csvData = getCsvData();

            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>BLUE TEAM - Time log file</title>");
            writer.write("<style>");
            writer.write("body { font-family: Arial, sans-serif; background-color: #FFFFFF; color: #000000; padding: 20px; }");
            writer.write("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
            writer.write("th { background-color: #000000; color: #FFFFFF; text-align: left; padding: 10px; }");
            writer.write("td { padding: 10px; }");
            writer.write("</style>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1 style=\"color: #00A1E4;\">BLUE TEAM - Time log file</h1>");
            writer.write("<table>");
            writer.write("<tr>");
            writer.write("<th style=\"background-color: #00A1E4;\">USER</th>");
            writer.write("<th style=\"background-color: #00A1E4;\">TASK DESCRIPTION</th>");
            writer.write("<th style=\"background-color: #00A1E4;\">TIME</th>");
            writer.write("</tr>");

            for (CsvUserInfo userInfo : csvData) {
                String username = userInfo.getUsername();
                List<Task> userTasks = userInfo.getUserTaskList();

                if (!userTasks.isEmpty()) {
                    for (Task task : userTasks) {
                        // Если сегодняшняя дата
                        if (task.getDate().equals(LocalDate.now())) {
                            writer.write("<tr>");
                            writer.write("<td style=\"background-color: #FFFFFF; color: #003082;\">" + username + "</td>");
                            writer.write("<td style=\"background-color: #FFFFFF; color: #003082;\">" + task.getDescription() + "</td>");
                            writer.write("<td style=\"background-color: #FFFFFF; color: #003082;\">" + task.getHours() + "</td>");
                            writer.write("</tr>");
                        }
                    }
                }
            }

            writer.write("</table>");
            writer.write("</body>");
            writer.write("</html>");

            writer.flush();
            String htmlContent = writer.toString();
            System.out.println(htmlContent);
            return htmlContent.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveHtmlFile(byte[] fileContent) {

        String filePath = "file.html";

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePath;
    }

    @Override
    public String convertHtmlToPdf(String htmlFilePath) {
        try {
            String pdfFilePath = htmlFilePath.replace(".html", ".pdf");

            File htmlFile = new File(htmlFilePath);

            File pdfFile = new File(pdfFilePath);

            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(new FileInputStream(htmlFile),
                    new FileOutputStream(pdfFile), converterProperties);
            System.out.println("HTML to PDF - Success. Path to PDF: " + pdfFilePath);
            return pdfFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CsvUserInfo> getCsvData() {
        List<CsvUserInfo> csvData = new ArrayList<>();

        List<User> allUsers = new ArrayList<>(userDao.getAll());

        for (int i = 0; i < allUsers.size(); i++) {
            CsvUserInfo userInfo = new CsvUserInfo();
            User user = allUsers.get(i);

            List<Task> userTaskList = new ArrayList<>(taskDao.getAllTasksFromUser(user.getId()));

            userInfo.setUsername(user.getUsername());
            userInfo.setId(user.getId());
            userInfo.setUserTaskList(userTaskList);


            csvData.add(userInfo);
        }

        return csvData;
    }



    //FOR PSVM TESTS ONLY======================
//    private List<CsvUserInfo> getCsvDataTest() {
//        List<CsvUserInfo> csvData = new ArrayList<>();
//
//        // Пример данных пользователей и задач для создания csvData
//        User user1 = new User(1, "Alpha Bravo", "");
//        User user2 = new User(2, "Charlie Delta", "");
//
//        Task task1 = new Task(1, "Complete project A", LocalDate.now(), 8, 1);
//        Task task2 = new Task(2, "Review documents", LocalDate.now(), 4, 1);
//        Task task3 = new Task(3, "Prepare presentation", LocalDate.now(), 6, 2);
//
//        CsvUserInfo userInfo1 = new CsvUserInfo();
//        userInfo1.setId(user1.getId());
//        userInfo1.setUsername(user1.getUsername());
//        userInfo1.setUserTaskList(Arrays.asList(task1, task2));
//
//        CsvUserInfo userInfo2 = new CsvUserInfo();
//        userInfo2.setId(user2.getId());
//        userInfo2.setUsername(user2.getUsername());
//        userInfo2.setUserTaskList(Arrays.asList(task3));
//
//        csvData.add(userInfo1);
//        csvData.add(userInfo2);
//
//        return csvData;
//    }
    //FOR PSVM TESTS ONLY======================
}
