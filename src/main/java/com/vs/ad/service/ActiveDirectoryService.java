package com.vs.ad.service;

import java.util.List;

import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

public interface ActiveDirectoryService {

    List<User> getAllUsers();
    User getUser(String userName);
    List<String> getNestedUserGroups(String dn);
    
    List<Group> getAllGroups();
    Group getGroup(String groupName);
    List<String> getGroupMembers(String groupDn, boolean nested);
    
    boolean addUserToGroup(String userDN, String groupDN);
    
}
