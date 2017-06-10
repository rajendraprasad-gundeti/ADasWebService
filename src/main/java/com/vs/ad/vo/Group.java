package com.vs.ad.vo;

import java.util.ArrayList;
import java.util.List;

public class Group extends ActiveDirObject {

    private String groupName;
    private List<String> members;
    private List<String> groups;

    public static String getFilter() {
        return "(objectCategory=group)";
    }

    public static String getGroupFilter(String groupName) {
        return "(&(objectCategory=group)(sAMAccountName=" + groupName + "))";
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(String member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public static boolean setProperty(Group group, String name, String value) {
        switch (name.toLowerCase()) {
            case "memberof":
                group.addGroup(value);
                break;
            
            case "cn":
                group.setGroupName(value);
                break;
            
            case "displayname":
                group.setDisplayname(value);
                break;
            case "distinguishedname":
                group.setDistinguishedName(value);
                break;
            case "samaccountname":
                group.setSamAccountName(value);
                break;
            case "member":
                group.addMember(value);
                break;


            default:
                System.out.println("Missed key  " + name + " value  " + value);
                break;
        }
        return true;
    }
}
