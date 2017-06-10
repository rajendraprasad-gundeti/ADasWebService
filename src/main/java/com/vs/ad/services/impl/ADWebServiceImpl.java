package com.vs.ad.services.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.vs.ad.common.Utils;
import com.vs.ad.service.ActiveDirectoryService;
import com.vs.ad.services.ADWebService;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

@Component
@Path("/adservices")
public class ADWebServiceImpl implements ADWebService {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpHeaders headers;

    @SuppressWarnings("unused")
    private ServletContext context;

    @Autowired
    private ActiveDirectoryService service;
    private ApplicationContext appContext;

    @Context
    public void setContext(ServletContext context) {
        System.out.println("context is being initialized");
        this.context = context;
        appContext = (ApplicationContext) context.getAttribute("applicationContext");
        service = (ActiveDirectoryService) appContext.getBean("service");
        System.out.println("context initialization is success");

    }

    @POST
    @Path("/createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User obj) {
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("/getUser/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userName") final String userName) {
        System.out.println("received request for " + userName);

        User user = service.getUser(userName);
        return getResponse(user);
    }

    private Response getResponse(Object obj) {
        return Response.status(Status.OK).entity(Utils.getJson(obj)).build();
    }

    public Response deleteUser(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response lockUser(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response unlockUser(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response setPassword(String userName, Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response resetPassword(String userName, Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @GET
    @Path("/getUserGroups/{userName}")
    public Response getUserGroups(@PathParam("userName") final String userName,
            @QueryParam("recursive") final String recursive) {
        User user = service.getUser(userName);
        if (user != null) {
            if (Utils.isNullOrEmpty(recursive) || recursive.equalsIgnoreCase("false")) {
                return getResponse(user.getGroups());
            } else {
                List<String> groups = service.getNestedUserGroups(user.getDistinguishedName());
                return getResponse(groups);
            }
        } else {
            return getResponse(null);
        }
    }

    @GET
    @Path("/getUsers")
    public Response getAllUsers() {
        List<User> users = service.getAllUsers();
        return getResponse(users);
    }

    public Response createGroup(Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @GET
    @Path("/getGroup/{groupName}")
    public Response getGroup(@PathParam("groupName") final String groupName) {
        Group group = service.getGroup(groupName);
        return getResponse(group);
    }

    public Response deleteGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Response addUserToGroup(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response removeUserFromGroup(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }


    @GET
    @Path("/getGroups")
    public Response getAllGroups() {
        List<Group> groups = service.getAllGroups();
        return getResponse(groups);
    }

    @GET
    @Path("/getGroupMembers/{groupDN}")
    public Response getGroupMembers(@PathParam("groupDN") final String groupDN) {
        List<String> members = service.getGroupMembers(groupDN, false);
        return getResponse(members);
    }

    @GET
    @Path("/getGroupMembersByName/{groupName}")
    public Response getGroupMembersByName(String groupName) {
        Group group = service.getGroup(groupName);
        return getResponse(group.getMembers());
    }

    @Override
    public Response enableUser(String userDn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response disableUser(String userDn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response getUserDisabledstatus(String userDN) {
        // TODO Auto-generated method stub
        return null;
    }

}
