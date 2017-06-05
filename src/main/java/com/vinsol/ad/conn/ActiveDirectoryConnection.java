package com.vinsol.ad.conn;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.vinsol.ad.conn.creds.Credentials;
import com.vinsol.ad.conn.creds.impl.FixedADCredsReader;

public class ActiveDirectoryConnection {

    private DirContext dirContext;
    // this should be autowired
    private Credentials adCreds;

    public ActiveDirectoryConnection() {
        dirContext = SimpleConnectToActiveDirectory();
    }

    public void closeConnection() {
        if (dirContext != null) {
            try {
                dirContext.close();

            } catch (NamingException e) {

            }
            dirContext = null;
        }
    }

    public static void main(String[] args) {

        System.out.println("Hello");
    }

    public DirContext getDirContext() {
        return dirContext;
    }

    public DirContext SimpleConnectToActiveDirectory() {
        adCreds = new FixedADCredsReader().getCredentials();
        DirContext dirContext = null;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple mode
                                                            // connectin with AD
        env.put(Context.SECURITY_PRINCIPAL, adCreds.getUserName());
        env.put(Context.SECURITY_CREDENTIALS, adCreds.getPassword());
        env.put(Context.PROVIDER_URL, adCreds.getDomainName());
        env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");
        try {
            dirContext = new InitialDirContext(env);
            System.out.println("Connection to AD is successful");
        } catch (NamingException e) {
            System.out.println("Connection to AD is failed");
            e.printStackTrace();
        }
        return dirContext;
    }

}
