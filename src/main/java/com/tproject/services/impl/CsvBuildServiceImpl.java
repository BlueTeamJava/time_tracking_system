package com.tproject.services.impl;


import com.tproject.dao.impl.TaskDaoImpl;
import com.tproject.dao.impl.UserDaoImpl;
import com.tproject.entity.CsvUserInfo;
import com.tproject.entity.Task;
import com.tproject.entity.User;
import com.tproject.services.CsvBuildService;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvBuildServiceImpl implements CsvBuildService {
    private static CsvBuildServiceImpl instance;

    private UserDaoImpl userDao = UserDaoImpl.getInstance();

    private TaskDaoImpl taskDao = TaskDaoImpl.getInstance();

    public static CsvBuildServiceImpl getInstance() {
        CsvBuildServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (CsvBuildServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CsvBuildServiceImpl();
                }
            }
        }
        return localInstance;
    }


    //path arg example: "/dir/csv/"
//    @Override
//    public String buildFile(String path) {
//        //define full file path
//        String filePath = path + "JavaBlueTeam-"+LocalDate.now() + ".csv";
//        try (FileWriter writer = new FileWriter(filePath)) {
//            List<CsvUserInfo> csvData = getCsvData(); // get all data
//
//            for (CsvUserInfo userInfo : csvData) {
//                String username = userInfo.getUsername();
//                List<Task> userTasks = userInfo.getUserTaskList();
//
//                if (!userTasks.isEmpty()) {
//                    writer.append(username).append(","); // username - 1 column
//
//                    // User task list iteration
//                    for (Task task : userTasks) {
//                        // if today's date
//                        if (task.getDate().equals(LocalDate.now())) {
//                            writer.append(",").append(task.getDescription()).append(","); // task description - 2 column
//                            writer.append(String.valueOf(task.getHours())).append("\n"); // task time - 3 column
//                        }
//                    }
//                }
//            }
//            System.out.println("File saved successfully at: " + filePath);
//            return filePath;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public byte[] createFile() {
        try (StringWriter writer = new StringWriter()) {
            List<CsvUserInfo> csvData = getCsvData(); // get all data

            for (CsvUserInfo userInfo : csvData) {
                String username = userInfo.getUsername();
                List<Task> userTasks = userInfo.getUserTaskList();

                if (!userTasks.isEmpty()) {
                    writer.append(username); // username - 1 column

                    // User task list iteration
                    for (Task task : userTasks) {
                        // if today's date
                        if (task.getDate().equals(LocalDate.now())) {
                            writer.append(",").append(task.getDescription()).append(","); // task description - 2 column
                            writer.append(String.valueOf(task.getHours())); // task time - 3 column
                        }
                        writer.append("\n");
                    }
                }
            }

            writer.flush();
            String fileContent = writer.toString();
            return fileContent.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //path arg example: "/dir/csv/"
    @Override
    public String saveFile(byte[] fileData, String path) {
        String fileName = "JavaBlueTeam-" + LocalDate.now() + ".csv";
        String filePath = path + fileName;

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(fileData);
            System.out.println("File saved successfully at: " + filePath);
            return filePath;
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
}

