package com.vs.ad.services;

import javax.ws.rs.core.Response;

public interface ADWebService {
    // user
    Response createUser(Object obj);

    Response getUser(String userName);

    Response deleteUser(String userName);

    Response lockUser(String userName);

    Response unlockUser(String userName);

    Response setPassword(String userName, Object obj);

    Response resetPassword(String userName, Object obj);

    Response getAllUsers();
    
    Response getUserGroups(String userName,String recursive);

    // group
    Response createGroup(Object obj);

    Response getGroup(String groupName);

    Response deleteGroup(String groupName);

    Response addUserToGroup(String userName, String groupName);

    Response removeUserFromGroup(String userName, String groupName);

    Response getAllGroups();
    
    Response getGroupMembers(String groupDN);
    
    Response getGroupMembersByName(String groupName);



}
