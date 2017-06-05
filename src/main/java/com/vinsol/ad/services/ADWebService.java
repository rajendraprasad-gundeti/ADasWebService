package com.vinsol.ad.services;

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

    // group
    Response createGroup(Object obj);

    Response getGroup(String groupName);

    Response deleteGroup(String groupName);

    Response AddUserToGroup(String userName, String groupName);

    Response RemoveUserFromGroup(String userName, String groupName);



}
