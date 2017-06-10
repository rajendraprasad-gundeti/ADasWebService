package com.vs.ad.vo;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User extends ActiveDirObject {
    private String username;
    private String firstName;
    private String lastName;
    private String userPrincipalName;
    private String emailID;
    private String isAdmin;
    private Date createdAT;
    private Date lastLoggedAt;
    private List<String> groups;
    private String password;

    public static String[] getUserAttributeNames() {
        String[] attributes = {"givenName", "sn", "cn", "displayname", "lastLogon", "mail",
                "accountExpires", "whenCreated", "userPrincipalName", "memberOf", "unicodePwd",
                "sAMAccountName", "distinguishedName"};
        return attributes;
    }

    // https://social.technet.microsoft.com/wiki/contents/articles/5392.active-directory-ldap-syntax-filters.aspx
    public static String getFilter() {
        // return "(objectClass=User)";//(&(objectCategory=person)(objectClass=user))
        return "(sAMAccountType=805306368)";
    }

    public static String getUserFilter(String userName) {
        return "(&(objectCategory=person)(objectClass=User)(sAMAccountName=" + userName + "))";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getCreatedAT() {
        return createdAT;
    }

    public void setCreatedAT(Date createdAT) {
        this.createdAT = createdAT;
    }

    public Date getLastLoggedAt() {
        return lastLoggedAt;
    }

    public void setLastLoggedAt(Date lastLoggedAt) {
        this.lastLoggedAt = lastLoggedAt;
    }


    public User() {

    }



    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String group) {
        if (groups == null) {
            groups = new ArrayList<String>();
        }
        groups.add(group);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean setProperty(User user, String name, String value) {
        switch (name.toLowerCase()) {
            case "memberof":
                user.addGroup(value);
                break;
            case "givenname":
                user.setFirstName(value);
                break;
            case "cn":
                user.setUsername(value);
                break;
            case "sn":
                user.setLastName(value);
                break;
            case "displayname":
                user.setDisplayname(value);
                break;
            case "distinguishedname":
                user.setDistinguishedName(value);
                break;
            case "samaccountname":
                user.setSamAccountName(value);
                break;
            case "userprincipalname":
                user.setUserPrincipalName(value);
                break;

            // case "lastLogon":
            // try {
            // user.setLastLoggedAt(new Date(value));
            // } catch (Exception e) {
            //
            // e.printStackTrace();
            // }
            // break;


            default:
                System.out.println("Missed key  " + name + " value  " + value);
                break;
        }
        return false;
    }



}


