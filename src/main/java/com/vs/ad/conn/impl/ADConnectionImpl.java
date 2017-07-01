package com.vs.ad.conn.impl;

import java.util.Enumeration;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.vs.ad.conn.ADConnection;
import com.vs.ad.conn.creds.Credentials;
import com.vs.ad.conn.creds.impl.BasicADCredsReader;


public class ADConnectionImpl implements ADConnection {

    private DirContext dirContext;
    private String searchBase;
    private String domainName;
    // this should be autowired
    private Credentials adCreds;

    public ADConnectionImpl() {
        dirContext = getSSLADContext();
        if (dirContext == null) {
            System.out.println("ssl conecxt is null");
            dirContext = getSimpleADContext();
        }
    }

    public String getSearchBase() {
        return searchBase;
    }

    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
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

    public static void main(String[] args) throws NamingException {
        System.setProperty("javax.net.ssl.trustStore", "E:\\Srinivas_Vutukoori\\cacerts");

        ADConnectionImpl con = new ADConnectionImpl();
        String filter = "(objectClass=User)";
        String base = "DC=novelirs,DC=com";
        // String base = "DC=itogle.com,DC=com";
        String[] attributes = {"givenName", "sn", "lastLogon", "mail", "accountExpires",
                "whenCreated", "userPrincipalName", "memberOf", "unicodePwd", "sAMAccountName"};
        NamingEnumeration<SearchResult> values = con.searchAD(base, filter, attributes);

        while (values.hasMoreElements()) {
            SearchResult result;
            result = (SearchResult) values.next();
            Attributes attribs = result.getAttributes();

            if (null != attribs) {
                for (NamingEnumeration<?> ae = attribs.getAll(); ae.hasMoreElements();) {
                    Attribute atr = (Attribute) ae.next();
                    String attributeID = atr.getID();
                    for (Enumeration<?> vals = atr.getAll(); vals.hasMoreElements();) {
                        String atrib_value = (String) vals.nextElement();
                        System.out.println(attributeID + "\t" + atrib_value);
                    }
                }
            }
        }



    }

    private NamingEnumeration<SearchResult> searchAD(String base, String filter,
            String[] attributes) {
        System.out.println("searching AD");
        NamingEnumeration<SearchResult> search_result;
        DirContext adContext;
        try {
            adContext = getSimpleADContext();
            SearchControls searchCtrls = new SearchControls();
            searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchCtrls.setReturningAttributes(attributes);
            search_result = adContext.search(base, filter, searchCtrls);
            adContext.close();
            System.out.println("returning results " + search_result);
            return search_result;
        } catch (NamingException e) {
            return null;
        }
    }

    public DirContext getDirContext() {
        return dirContext;
    }

    public DirContext getSimpleADContext() {
        adCreds = new BasicADCredsReader().getCredentials();
        DirContext dirContext = null;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple mode
                                                            // connectin with AD
        env.put(Context.SECURITY_PRINCIPAL, adCreds.getUserName() + "@" + adCreds.getDomainName());
        env.put(Context.SECURITY_CREDENTIALS, adCreds.getPassword());
        env.put(Context.PROVIDER_URL,
                "ldap://" + adCreds.getServerName() + ":" + adCreds.getPort());
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

    public DirContext getSSLADContext() {
        adCreds = new BasicADCredsReader().getCredentials();
        searchBase = adCreds.getSearchBase();
        setDomainName(adCreds.getDomainName());
        DirContext dirContext = null;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // simple mode
                                                            // connectin with AD
        env.put(Context.SECURITY_PROTOCOL, "ssl"); // use SSL security protocol to make connection
                                                   // with AD

        env.put(Context.SECURITY_PRINCIPAL, adCreds.getUserName() + "@" + adCreds.getDomainName());
        env.put(Context.SECURITY_CREDENTIALS, adCreds.getPassword());
        env.put(Context.PROVIDER_URL,
                "ldap://" + adCreds.getServerName() + ":" + adCreds.getSslPort());
        env.put("com.sun.jndi.ldap.connect.pool", "false");
        env.put("com.sun.jndi.ldap.connect.timeout", "300000");
        try {
            dirContext = new InitialDirContext(env);
            
            System.out.println("Connection to AD in ssl  is successful");
        } catch (NamingException e) {
            System.out.println("Connection to AD is failed");
            e.printStackTrace();
        }
        return dirContext;
    }

    public DirContext getDirectoryContext() {

        return dirContext;
    }

    public String getDomainName() {
        domainName = adCreds.getDomainName();
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    
}
