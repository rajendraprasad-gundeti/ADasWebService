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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vs.ad.conn.ADConnection;
import com.vs.ad.conn.impl.ADSearchUtils;
import com.vs.ad.service.ActiveDirectoryService;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {

    // https://msdn.microsoft.com/en-us/library/aa772300(v=vs.85).aspx
    private static final int ADS_UF_ACCOUNTDISABLE = 0x02;

    @Autowired
    ADConnection conn;
    // String base = "DC=novelirs,DC=com";
    // String base = "DC=itogle,DC=com";

    @Autowired
    ADSearchUtils searchUtils;

    public List<User> getAllUsers() {
        return searchUtils.searchUsers(conn.getSearchBase());
    }


    public User getUser(String userName) {
        User user = searchUtils.searchUser(conn.getSearchBase(), userName);
        return user;
    }


    @Override
    public List<String> getNestedUserGroups(String dn) {
        return searchUtils.searchUserGroups(conn.getSearchBase(), dn);
    }


    @Override
    public List<Group> getAllGroups() {
        return searchUtils.searchGroups(conn.getSearchBase());
    }


    @Override
    public Group getGroup(String groupName) {
        return searchUtils.searchGroup(conn.getSearchBase(), groupName);
    }


    @Override
    public List<String> getGroupMembers(String groupDn, boolean nested) {
        return searchUtils.searchGroupMembers(conn.getSearchBase(), groupDn, nested);
    }

    public boolean addUserToGroup(String userDN, String groupDN) {
        boolean status = false;
        try {
            ModificationItem mods[] = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
                    new BasicAttribute("member", userDN));
            conn.getDirectoryContext().modifyAttributes(groupDN, mods);
            System.out.println(userDN + " is added to a member of " + groupDN + " group");
            status = true;
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Problem adding member: " + e);
            return status;
        }
        return false;
    }

    public boolean removeUserFromGroup(String userDN, String groupDN) {
        boolean status = false;
        try {
            ModificationItem mods[] = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
                    new BasicAttribute("member", userDN));
            conn.getDirectoryContext().modifyAttributes(groupDN, mods);
            System.out.println(userDN + " is removed from " + groupDN + " group");
            status = true;
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Problem adding member: " + e);
            return status;
        }
        return false;
    }


    public boolean createUser(User user) {
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
        byte[] UnicodePassword = EncodePassword(user.getPassword());// encode the user password
        Attribute pass = new BasicAttribute("unicodePwd", UnicodePassword);
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
        try {
            DirContext context = conn.getDirectoryContext()
                    .createSubcontext(user.getDistinguishedName(), container);
            if (context != null) {
                System.out.println("successfully add user to active directory");
                return true;
            } else {
                System.out.println("failed to add user to ad");
                return false;
            }
        } catch (NamingException e) {
            System.out.println("There is an error, cannot add new user");
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteUser(String userDN) {
        try {
            conn.getDirectoryContext().destroySubcontext(userDN);
            System.out.println("user is successfully deleted");
            return true;
        } catch (NamingException e) {
            e.printStackTrace();
            System.out.println("Error,cannot delete the user");
            return false;
        }
    }

    public boolean setPassword(String userDN, String password) {
        byte pwdArray[] = EncodePassword(password);
        try {
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("UnicodePwd", pwdArray));
            conn.getDirectoryContext().modifyAttributes(userDN, mods);
            System.out.println("user password updated!");
            return true;
        } catch (NamingException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("update password error: " + e);
            return false;
        }
    }

    public boolean enableUser(String userDN) {
        long status = getUserDisabledStatus(userDN);
        status = status & ~ADS_UF_ACCOUNTDISABLE;

        return updateUserAttrValue(userDN, uac, "" + status);
    }

    public boolean disableUser(String userDn) {
        long status = getUserDisabledStatus(userDn);
        status = status | ADS_UF_ACCOUNTDISABLE;
        return updateUserAttrValue(userDn, uac, "" + status);
    }

    private static String uac = "userAccountControl";

    public long getUserDisabledStatus(String userDn) {
        String uacValue = getUserAttrValue(userDn, "userAccountControl");
        try {
            return Long.parseLong(uacValue);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }


    private boolean updateUserAttrValue(String userDn, String attrName, String attrValue) {
        try {
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute(attrName, attrValue));
            conn.getDirectoryContext().modifyAttributes(userDn, mods);
            return true;
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println();
            return false;
        }
    }

    private String getUserAttrValue(String userDn, String attrName) {

        try {
            Attributes attrs =
                    conn.getDirectoryContext().getAttributes(userDn, new String[] {attrName});
            Attribute attr = attrs.get(attrName);
            if (attr != null) {
                return attr.get().toString();
            }
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
