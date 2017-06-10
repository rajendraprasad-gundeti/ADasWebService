package com.vs.ad.conn.creds.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.vs.ad.conn.creds.ADCredsReader;
import com.vs.ad.conn.creds.Credentials;

public class BasicADCredsReader implements ADCredsReader {

    public Credentials getCredentials() {
        Properties props = loadProps();
        Credentials creds = new Credentials();
        creds.setUserName(props.getProperty("ad.adminuser"));
        creds.setPassword(props.getProperty("ad.password"));
        creds.setDomainName(props.getProperty("ad.domain"));
        creds.setServerName(props.getProperty("ad.serverip"));
        String port = props.getProperty("ad.port", "389");
        String sslport = props.getProperty("ad.sslport", "636");
        

        creds.setPort(Integer.parseInt(port));
        creds.setSslPort(Integer.parseInt(sslport));
        creds.setSearchBase(props.getProperty("ad.searchbase"));
        return creds;
    }


    private Properties loadProps() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "connection.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return null;
            }

            prop.load(input);
            System.out.println(prop);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
}

