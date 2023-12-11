package com.tproject.services.impl;

import com.tproject.mailer.GMailer;
import com.tproject.services.ScheduledService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledCsvServiceImpl implements ScheduledService {
    private Timer timer;
    private static ScheduledCsvServiceImpl instance;
    public static ScheduledCsvServiceImpl getInstance() {
        ScheduledCsvServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (ScheduledCsvServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ScheduledCsvServiceImpl();
                }
            }
        }
        return localInstance;
    }
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
                    try {
                        new GMailer().sendMail();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //TODO: refactor GMailer into singletone in order to to multiply instances
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


    private Date getNextExecutionTime() {
        // current date and time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // set as 19.00 UTC
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 50);
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
