package com.vs.ad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vs.ad.conn.ADConnection;
import com.vs.ad.conn.impl.ADSearchUtils;
import com.vs.ad.service.ActiveDirectoryService;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {

    @Autowired
    ADConnection conn;
    String base = "DC=novelirs,DC=com";
    // String base = "DC=itogle,DC=com";

    @Autowired
    ADSearchUtils searchUtils;

    public List<User> getAllUsers() {
        return searchUtils.searchUsers(base);
    }


    public User getUser(String userName) {
        User user = searchUtils.searchUser(base, userName);
        return user;
    }


    @Override
    public List<String> getNestedUserGroups(String dn) {
        return searchUtils.searchUserGroups(base, dn);
    }


    @Override
    public List<Group> getAllGroups() {
        return searchUtils.searchGroups(base);
    }


    @Override
    public Group getGroup(String groupName) {
        return searchUtils.searchGroup(base, groupName);
    }


    @Override
    public List<String> getGroupMembers(String groupDn, boolean nested) {
        return searchUtils.searchGroupMembers(base, groupDn, nested);
    }



}
