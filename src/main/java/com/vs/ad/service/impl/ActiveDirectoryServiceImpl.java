package com.vs.ad.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vs.ad.common.exec.ADErrorCode;
import com.vs.ad.common.exec.ADException;
import com.vs.ad.conn.ADConnection;
import com.vs.ad.conn.impl.ADSearchUtils;
import com.vs.ad.service.ActiveDirectoryService;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {
    private static Logger LOGGER = LoggerFactory.getLogger(ActiveDirectoryServiceImpl.class);

    private static String uac = "userAccountControl";

    @Autowired
    ADConnection conn;
    // String base = "DC=novelirs,DC=com";
    // String base = "DC=itogle,DC=com";

    @Autowired
    ADSearchUtils searchUtils;

    public List<User> getAllUsers() throws ADException {
        LOGGER.info("Searching for all users");
        return searchUtils.searchUsers();
    }


    public User getUser(String userName) throws ADException {
        LOGGER.info("searching for user '{}'", userName);
        User user = searchUtils.searchUser(userName);
        return user;
    }


    @Override
    public List<String> getNestedUserGroups(String dn) {
        return searchUtils.searchUserGroups(dn);
    }


    @Override
    public List<Group> getAllGroups() {
        return searchUtils.searchGroups();
    }


    @Override
    public Group getGroup(String groupName) {
        return searchUtils.searchGroup(groupName);
    }


    @Override
    public List<String> getGroupMembers(String groupDn, boolean nested) {
        return searchUtils.searchGroupMembers(groupDn, nested);
    }

    public boolean addUserToGroup(String userDN, String groupDN) throws ADException {

        try {
            ModificationItem mods[] = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
                    new BasicAttribute("member", userDN));
            conn.getDirectoryContext().modifyAttributes(groupDN, mods);
            LOGGER.info(userDN + " is added to a member of " + groupDN + " group");
            return true;
        } catch (NamingException e) {
            LOGGER.error("Adding user '{}' to group '{}' failed with exception '{}'", userDN,
                    groupDN, e.getMessage());
            throw new ADException("Adding user '" + userDN + "' to group '" + groupDN + "' failed",
                    ADErrorCode.OTHER, e);

        }
    }

    public boolean removeUserFromGroup(String userDN, String groupDN) throws ADException {

        try {
            ModificationItem mods[] = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
                    new BasicAttribute("member", userDN));
            conn.getDirectoryContext().modifyAttributes(groupDN, mods);
            LOGGER.info(userDN + " is removed from " + groupDN + " group");
            return true;
        } catch (NamingException e) {
            LOGGER.error("Removing user '{}' from group '{}' failed with exception '{}'", userDN,
                    groupDN, e.getMessage());
            throw new ADException(
                    "Removing user '" + userDN + "' from group '" + groupDN + "' failed",
                    ADErrorCode.OTHER, e);
        }

    }


    public boolean createUser(User user, String randomPwd) throws ADException {
        Attributes container = new BasicAttributes();
        // Create the objectclass to add
        Attribute objClasses = new BasicAttribute("objectClass");
        objClasses.add("top");
        objClasses.add("person");
        objClasses.add("organizationalPerson");
        objClasses.add("user");
        // Assign the username, first name, and last name
        String cnValue = new StringBuffer(user.getFirstName()).append(" ")
                .append(user.getLastName()).toString();
        Attribute cn = new BasicAttribute("cn", cnValue);
        Attribute sAMAccountName = new BasicAttribute("sAMAccountName", user.getSamAccountName());
        Attribute principalName = new BasicAttribute("userPrincipalName",
                user.getName() + "@" + conn.getDomainName());
        Attribute givenName = new BasicAttribute("givenName", user.getFirstName());
        Attribute sn = new BasicAttribute("sn", user.getLastName());
        Attribute uid = new BasicAttribute("uid", user.getName());
        Attribute displayname =
                new BasicAttribute("displayname", user.getFirstName() + " " + user.getLastName());
        Attribute email = new BasicAttribute("mail", user.getEmailID());
        byte[] UnicodePassword = EncodePassword(randomPwd);// encode the user password
        Attribute pass = new BasicAttribute("unicodePwd", UnicodePassword);
        Attribute uacAttr = new BasicAttribute(uac, "512");
        // Attribute pwdLastSet = new BasicAttribute("pwdLastSet","-1");
        // Add these to the container
        container.put(objClasses);
        container.put(sAMAccountName);
        container.put(principalName);
        container.put(cn);
        container.put(sn);
        container.put(givenName);
        container.put(uid);
        container.put(displayname);
        container.put(email);
        container.put(pass);
        container.put(uacAttr);
        // container.put("pwdLastSet", pwdLastSet);
        try {
            DirContext context = conn.getDirectoryContext()
                    .createSubcontext(user.getDistinguishedName(), container);
            if (context != null) {
                LOGGER.info("successfully add user to active directory");
                return true;
            } else {
                LOGGER.error("failed to create user to ad");
                throw new ADException("Failed to create user in AD", ADErrorCode.EU0003);
            }
        } catch (NamingException e) {
            LOGGER.error("There is an exception while creating user '{}'", e.getMessage(), e);
            throw new ADException("User creation failed ", ADErrorCode.EU0003, e);

        }

    }

    public boolean deleteUser(String userDN) throws ADException {
        try {
            return delete(userDN);
        } catch (ADException e) {
            throw new ADException("cannot delete user", ADErrorCode.EU0005, e);
        }
    }

    public boolean deleteGroup(String groupDN) throws ADException {
        try {
            return delete(groupDN);
        } catch (ADException e) {
            throw new ADException("cannot delete group", ADErrorCode.EU0005, e);
        }
    }


    private boolean delete(String groupDN) throws ADException {
        try {
            conn.getDirectoryContext().destroySubcontext(groupDN);
            LOGGER.info(" '{}' successfully deleted", groupDN);
            return true;
        } catch (NamingException e) {
            LOGGER.error("Error,cannot delete ");
            throw new ADException("cannot delete", ADErrorCode.EU0005, e);
        }
    }

    public boolean setPassword(String userDN, String password) throws ADException {
        LOGGER.info("Setting password for user '{}' ", userDN);
        byte pwdArray[] = EncodePassword(password);
        try {
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("UnicodePwd", pwdArray));
            conn.getDirectoryContext().modifyAttributes(userDN, mods);
            LOGGER.info("user '{}' password updated!", userDN);
            return true;
        } catch (NamingException | NullPointerException e) {

            LOGGER.error("Password update failed for user '{}', exception is '{}'", userDN,
                    e.getMessage(), e);
            throw new ADException("Password update is failed for user " + userDN,
                    ADErrorCode.EU0004, e);
        }
    }

    public boolean enableUser(String userDN) throws ADException {

        long status = getUserUACStatus(userDN);
        if (status == (status & ~ADUserFlags.DISABLED.getCode())) {
            LOGGER.info("User account '{}' is already enaled ", userDN);
            return false;
        }
        status = status & ~ADUserFlags.DISABLED.getCode();

        try {
            return updateUserAttrValue(userDN, uac, "" + status);
        } catch (ADException e) {
            LOGGER.error(
                    "Exception while enabling user account '{}', exception is '{}', error code '{}'",
                    userDN, e.getMessage(), e.getErrorCode(), e);
            throw new ADException("Failed to enable user account for user '" + userDN + "'",
                    ADErrorCode.EU0007, e);
        }
    }

    public boolean disableUser(String userDn) throws ADException {
        long status = getUserUACStatus(userDn);
        if (status == (status | ADUserFlags.DISABLED.getCode())) {
            LOGGER.info("User account '{}' is already disbaled ", userDn);
            return false;
        }
        status = status | ADUserFlags.DISABLED.getCode();
        try {
            return updateUserAttrValue(userDn, uac, "" + status);
        } catch (ADException e) {
            LOGGER.error(
                    "Exception while disabling user account '{}', exception is '{}', error code '{}'",
                    userDn, e.getMessage(), e.getErrorCode(), e);
            throw new ADException("Failed to disable user account for user '" + userDn + "'",
                    ADErrorCode.EU0008, e);
        }
    }

    public boolean getUserDisabledStatus(String userDn) throws ADException {
        long uacValue = getUserUACStatus(userDn);
        return (uacValue | ADUserFlags.DISABLED.getCode()) == uacValue;
    }

    public long getUserUACStatus(String userDn) throws ADException {
        String uacValue = getUserAttrValue(userDn, "userAccountControl");
        try {
            return Long.parseLong(uacValue);
        } catch (NumberFormatException e) {
            throw new ADException("User Account Control value is not a long for " + userDn,
                    ADErrorCode.EU0008, e);
        }
    }

    public boolean unlockUser(String userDN) throws ADException {

        long status = getUserUACStatus(userDN);
        if (status == (status & ~ADUserFlags.LOCKED.getCode())) {
            LOGGER.info("User account '{}' is already unlocked ", userDN);
            return false;
        }
        status = status & ~ADUserFlags.LOCKED.getCode();

        try {
            return updateUserAttrValue(userDN, uac, "" + status);
        } catch (ADException e) {
            LOGGER.error(
                    "Exception while unlocking user account '{}', exception is '{}', error code '{}'",
                    userDN, e.getMessage(), e.getErrorCode(), e);
            throw new ADException("Failed to unlock user account for user '" + userDN + "'",
                    ADErrorCode.EU0010, e);
        }
    }

    public boolean lockUser(String userDn) throws ADException {
        long status = getUserUACStatus(userDn);
        if (status == (status | ADUserFlags.LOCKED.getCode())) {
            LOGGER.info("User account '{}' is already locked ", userDn);
            return false;
        }
        status = status | ADUserFlags.LOCKED.getCode();
        try {
            return updateUserAttrValue(userDn, uac, "" + status);
        } catch (ADException e) {
            LOGGER.error(
                    "Exception while locking user account '{}', exception is '{}', error code '{}'",
                    userDn, e.getMessage(), e.getErrorCode(), e);
            throw new ADException("Failed to lock user account for user '" + userDn + "'",
                    ADErrorCode.EU0011, e);
        }
    }


    private boolean updateUserAttrValue(String userDn, String attrName, String attrValue)
            throws ADException {
        LOGGER.info("Updating user '{}' attribute '{}' value '{}'", userDn, attrName, attrValue);
        try {
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute(attrName, attrValue));
            conn.getDirectoryContext().modifyAttributes(userDn, mods);
            LOGGER.info("User '{}' attribute '{}' is successfully updated", userDn, attrName);
            return true;
        } catch (NamingException e) {
            throw new ADException("User attribute update is failed for '" + userDn + "' attribute '"
                    + attrName + "'", ADErrorCode.EU0006, e);
        }
    }

    private String getUserAttrValue(String userDn, String attrName) throws ADException {

        try {
            Attributes attrs =
                    conn.getDirectoryContext().getAttributes(userDn, new String[] {attrName});
            Attribute attr = attrs.get(attrName);
            if (attr != null) {
                return attr.get().toString();
            }
        } catch (NamingException e) {
            throw new ADException(
                    "Retrieval of user '" + userDn + "' attribute '" + attrName + "' is failed",
                    ADErrorCode.EU0009, e);
        }
        return null;
    }

    private byte[] EncodePassword(String password) {
        String temp = "\"" + password + "\"";
        byte[] UnicodePassword = null;
        try {
            UnicodePassword = temp.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Problem encoding password: " + e);
            e.printStackTrace();
        }
        return UnicodePassword;// return the encoded password
    }



}
