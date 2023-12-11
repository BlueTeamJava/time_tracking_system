package com.tproject.servlets;

import com.tproject.services.impl.ScheduledCsvServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ScheduledCsvServiceImpl.getInstance().startScheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
