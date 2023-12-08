package com.tproject.services.impl;

import com.tproject.services.ScheduledCsvService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//can be invoked in Dispatcher like

//        ScheduledCsvServiceImpl scheduledCsvServiceImpl = new ScheduledCsvServiceImpl();
//        ScheduledCsvServiceImpl.startScheduler();


public class ScheduledCsvServiceImpl implements ScheduledCsvService {
    private Timer timer;

    @Override
    public void startScheduler() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                    buildFile();
                }
            }
        }, getNextExecutionTime(), 24 * 60 * 60 * 1000); // every working day. time is set in getNextExecutionTime() method
    }

    @Override
    public void stopScheduler() {
        if (timer != null) {
            timer.cancel();
        }
    }


    private void buildFile(){


        //example data, must be replaced

//        user   | name     | descr    | time
//        -----------------------------------
//        user1  | task1-1  | descr1-1 | time1-1
//               | task1-2  | descr1-2 | time1-2
//        user2  | task2-1  | descr2-1 | time2-1
//               | task2-2  | descr2-2 | time2-2

        Object[][] data = {
                {"user1", "task", "Email"},
                {"user2", "30", "johndoe@example.com"},
                {"user3", "25", "janesmith@example.com"}
        };

        try (FileWriter writer = new FileWriter("output.csv")) {
            for (Object[] rowData : data) {
                for (Object field : rowData) {
                    writer.append(field.toString());
                    writer.append(",");
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date getNextExecutionTime() {
        // current date and time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // set as 19.00 UTC
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // is current time passed
        Date currentTime = calendar.getTime();
        if (currentTime.after(new Date())) {
            return currentTime;
        } else {
            // current date increase by one day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }
    }

}
