package com.vs.ad.services.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.vs.ad.common.Utils;
import com.vs.ad.common.exec.ADException;
import com.vs.ad.service.ActiveDirectoryService;
import com.vs.ad.services.ADWebService;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

@Component
@Path("/adservices")
public class ADWebServiceImpl implements ADWebService {
    private static Logger LOGGER = LoggerFactory.getLogger(ADWebServiceImpl.class);

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
        LOGGER.info("context is being initialized");
        this.context = context;
        appContext = (ApplicationContext) context.getAttribute("applicationContext");
        service = (ActiveDirectoryService) appContext.getBean("service");
        LOGGER.info("context initialization is success");

    }

    @POST
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        LOGGER.info("Received request to create user with properties '{}'", user);
        try {
            String randomPwd = User.generateRandomPassword();
            boolean status = service.createUser(user, randomPwd);
            if (status) {
                LOGGER.info("User creation is successful");
                return getResponse(
                        "User creation is successful and default password is : " + randomPwd);
            } else {
                // this will never happen
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }

    }

    @GET
    @Path("/users/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userName") final String userName) {
        LOGGER.info("received request for " + userName);
        try {
            User user = service.getUser(userName);
            return getResponse(user);
        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }

    }

    @DELETE
    @Path("/users/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userName") final String userName) {
        try {
            boolean status = service.deleteUser(userName);
            if (status) {
                LOGGER.info("User deletion is successful");
                return getResponse("User deletion is successful");
            } else {
                // this will never happen
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/lock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response lockUser(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to lock user account for '{}'", userName);
        try {
            boolean status = service.lockUser(userName);
            if (status) {
                LOGGER.info("User account '{}' is locked", userName);
                return getResponse("User account '" + userName + "' is locked");
            } else {

                return getResponse("User account '" + userName + "' is already locked");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/unlock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unlockUser(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to enable user account for '{}'", userName);
        try {
            boolean status = service.unlockUser(userName);
            if (status) {
                LOGGER.info("User account '{}' is unlocked", userName);
                return getResponse("User account '" + userName + "' is unlocked");
            } else {

                return getResponse("User account '" + userName + "' is already unlocked");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }

    }


    @POST
    @Path("/users/{userName}/enable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enableUser(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to enable user account for '{}'", userName);
        try {
            boolean status = service.enableUser(userName);
            if (status) {
                LOGGER.info("User account '{}' is enabled", userName);
                return getResponse("User account '" + userName + "' is enabled");
            } else {

                return getResponse("User account '" + userName + "' is already enabled");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/disable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response disableUser(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to disable user account for '{}'", userName);
        try {
            boolean status = service.disableUser(userName);
            if (status) {
                LOGGER.info("User account '{}' is disabled", userName);
                return getResponse("User account '" + userName + "' is disabled");
            } else {
                return getResponse("User account '" + userName + "' is already disabled");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @GET
    @Path("/users/{userName}/disabledstatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserDisabledstatus(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to disable user account for '{}'", userName);
        try {
            boolean status = service.getUserDisabledStatus(userName);
            if (status) {
                LOGGER.info("User account '{}' is disabled", userName);
                return getResponse("User account '" + userName + "' is currently disabled");
            } else {
                return getResponse("User account '" + userName + "' is currently not disabled");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }

    }

    @GET
    @Path("/users/{userName}/accountstatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserAccountstatus(@PathParam("userName") final String userName) {
        LOGGER.info("Received request to disable user account for '{}'", userName);
        try {
            long status = service.getUserUACStatus(userName);
            LOGGER.info("User account '{}' is value is '{}'", status);
            return getResponse("User account '" + userName + "' current status is " + status);
        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }

    }


    @POST
    @Path("/users/{userName}/setpassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setPassword(@PathParam("userName") final String userName, String password) {
        try {
            boolean status = service.setPassword(userName, password);
            if (status) {
                LOGGER.info("Password update is successful for user '{}'", userName);
                return getResponse("Password update is successful");
            } else {
                // this will never happen
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/resetpassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPassword(@PathParam("userName") final String userName, String password) {
        return setPassword(userName, password);
    }


    @GET
    @Path("/users/{userName}/usergroups")
    public Response getUserGroups(@PathParam("userName") final String userName,
            @QueryParam("recursive") final String recursive) {
        User user;
        try {
            user = service.getUser(userName);

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
        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<User> users;
        try {
            users = service.getAllUsers();
            return getResponse(users);
        } catch (ADException e) {
            return getADExceptionResponse(e);
        }

    }

    @POST
    @Path("/groups")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public Response createGroup(Object obj) {
        return Response.status(Status.OK).entity("not implemented yet").build();
    }

    @GET
    @Path("/groups/{groupName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroup(@PathParam("groupName") final String groupName) {
        Group group;
        try {
            group = service.getGroup(groupName);

            return getResponse(group);
        } catch (ADException e) {
            return getADExceptionResponse(e);
        }
    }

    @DELETE
    @Path("/groups/{groupName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGroup(@PathParam("groupName") final String groupName) {
        try {
            boolean status = service.deleteGroup(groupName);
            if (status) {
                LOGGER.info("group deletion is successful");
                return getResponse("group deletion is successful  " + groupName);
            } else {
                // this will never happen
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/addtogroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserToGroup(@PathParam("userName") final String userName, String groupName) {
        LOGGER.info("Received request to add user '{}' to group '{}'", userName, groupName);
        try {
            boolean status = service.addUserToGroup(userName, groupName);
            if (status) {
                LOGGER.info("user '{}' is successfully added to group '{}'", userName, groupName);
                return getResponse("User '" + userName + "' is successfully added to group '"
                        + groupName + "'");
            } else {

                return getResponse(
                        "Failed to add user '" + userName + "' to group '" + groupName + "'");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }

    @POST
    @Path("/users/{userName}/removefromgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUserFromGroup(@PathParam("userName") final String userName,
            String groupName) {
        LOGGER.info("Received request to remove user '{}' from group '{}'", userName, groupName);
        try {
            boolean status = service.removeUserFromGroup(userName, groupName);
            if (status) {
                LOGGER.info("user '{}' is successfully removed to group '{}'", userName, groupName);
                return getResponse("User '" + userName + "' is successfully removed to group '"
                        + groupName + "'");
            } else {

                return getResponse(
                        "Failed to remove user '" + userName + "' to group '" + groupName + "'");
            }

        } catch (ADException ade) {
            return getADExceptionResponse(ade);
        } catch (Exception e) {
            return getGenericExceptionResponse(e);
        }
    }


    @GET
    @Path("/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGroups() {
        List<Group> groups;
        try {
            groups = service.getAllGroups();

            return getResponse(groups);
        } catch (ADException e) {
            return getADExceptionResponse(e);
        }
    }

    @GET
    @Path("/groups/{groupDN}/groupmembers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupMembers(@PathParam("groupDN") final String groupDN) {
        List<String> members;
        try {
            members = service.getGroupMembers(groupDN, false);

            return getResponse(members);
        } catch (ADException e) {
            return getADExceptionResponse(e);
        }
    }

    @GET
    @Path("/groups/{groupName}/groupmembersbyname")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupMembersByName(String groupName) {
        Group group;
        try {
            group = service.getGroup(groupName);

            return getResponse(group.getMembers());
        } catch (ADException e) {
            return getADExceptionResponse(e);
        }
    }



    private Response getGenericExceptionResponse(Exception e) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Exception " + e.getMessage())
                .build();
    }

    private Response getADExceptionResponse(ADException ade) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Error  " + ade.getErrorCode() + " exception " + ade.getMessage()).build();
    }

    private Response getResponse(Object obj) {
        LOGGER.trace("returning response '{}'", Utils.getJson(obj));
        return Response.status(Status.OK).entity(Utils.getJson(obj)).build();
    }


}
