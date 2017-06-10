package com.vs.ad;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;


public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext appContext = event.getApplicationContext();
        WebApplicationContext webAppContext = (WebApplicationContext) appContext;

        ServletContext sc = webAppContext.getServletContext();
        sc.setAttribute("applicationContext", appContext);


    }

}
