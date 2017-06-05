package com.vinsol.ad.conn.creds.impl;

import com.vinsol.ad.conn.creds.ActiveDirectoryCredsReader;
import com.vinsol.ad.conn.creds.Credentials;

public class FixedADCredsReader implements ActiveDirectoryCredsReader {

    public Credentials getCredentials() {
        Credentials creds = new Credentials();
        creds.setUserName("administrator");
        creds.setPassword("password");// This should be a encrypted password,
        creds.setDomainName("domain");
        return creds;
    }

}
