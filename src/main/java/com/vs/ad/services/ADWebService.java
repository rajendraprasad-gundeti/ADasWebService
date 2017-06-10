package com.vs.ad.services;

import javax.ws.rs.core.Response;

import com.vs.ad.vo.User;

public interface ADWebService {
    // user
    Response createUser(User obj);

    Response getUser(String userName);

    Response deleteUser(String userName);

    Response lockUser(String userName);

    Response unlockUser(String userName);

    Response setPassword(String userName, Object obj);

    Response resetPassword(String userName, Object obj);
    
    Response enableUser(String userDn);
    
    Response disableUser(String userDn);
    Response getUserDisabledstatus(String userDN);

    Response getAllUsers();
    
    Response getUserGroups(String userName,String recursive);

    // group
    Response createGroup(Object obj);

    Response getGroup(String groupName);

    Response deleteGroup(String groupName);

    Response addUserToGroup(String userDN, String groupDN);

    Response removeUserFromGroup(String userDN, String groupDN);

    Response getAllGroups();
    
    Response getGroupMembers(String groupDN);
    
    Response getGroupMembersByName(String groupName);



}
