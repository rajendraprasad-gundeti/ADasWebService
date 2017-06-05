package com.vinsol.ad.services.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vinsol.ad.services.ADWebService;

@Path("/adservices")
public class ADWebServiceImpl implements ADWebService {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpHeaders headers;

    @SuppressWarnings("unused")
    private ServletContext context;

    @Context
    public void setContext(ServletContext context) {
        this.context = context;

    }

    @POST
    @Path("/createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(Object obj) {
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("/getUser/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userName") final String userName) {
        return Response.status(Status.OK).build();
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

    public Response createGroup(Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response getGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response deleteGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response AddUserToGroup(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    public Response RemoveUserFromGroup(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

}
