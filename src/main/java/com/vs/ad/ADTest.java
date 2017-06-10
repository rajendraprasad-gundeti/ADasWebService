package com.vs.ad;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vs.ad.services.ADWebService;

public class ADTest {

    static ApplicationContext context;

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("spring-context.xml");
        ADWebService endpoint = (ADWebService) context.getBean("endpoint");
        endpoint.getUser("");
        

    }

}
