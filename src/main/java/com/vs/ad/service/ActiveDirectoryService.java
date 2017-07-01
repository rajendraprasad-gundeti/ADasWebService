package com.vs.ad.service;

import java.util.List;

import com.vs.ad.common.exec.ADException;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

public interface ActiveDirectoryService {

    List<User> getAllUsers() throws ADException;

    User getUser(String userName) throws ADException;

    boolean createUser(User user, String randomPwd) throws ADException;

    List<String> getNestedUserGroups(String dn) throws ADException;

    List<Group> getAllGroups() throws ADException;

    Group getGroup(String groupName) throws ADException;

    List<String> getGroupMembers(String groupDn, boolean nested) throws ADException;

    boolean addUserToGroup(String userDN, String groupDN) throws ADException;

    boolean deleteUser(String userName) throws ADException;

    boolean setPassword(String userDN, String password) throws ADException;

    boolean enableUser(String userDN) throws ADException;

    boolean disableUser(String userDN) throws ADException;

    boolean unlockUser(String userDN) throws ADException;

    boolean lockUser(String userDN) throws ADException;

    boolean getUserDisabledStatus(String userDN) throws ADException;

    public long getUserUACStatus(String userDn) throws ADException;

    boolean removeUserFromGroup(String userDN, String groupDN) throws ADException;
    
    boolean deleteGroup(String groupName) throws ADException;



}
