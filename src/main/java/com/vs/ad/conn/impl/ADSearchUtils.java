package com.vs.ad.conn.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.vs.ad.conn.ADConnection;
import com.vs.ad.vo.Group;
import com.vs.ad.vo.User;

public class ADSearchUtils {

    @Autowired
    private ADConnection conn;

    public ADSearchUtils() {

    }

    public ADSearchUtils(ADConnection conn) {
        this.conn = conn;
    }



    public static NamingEnumeration<SearchResult> searchDir(DirContext adContext, String base,
            String filter, String[] attributes) {
        System.out.println("Searching Ad with base " + base + "'filter '" + filter
                + "' attributes '" + attributes + "'");
        NamingEnumeration<SearchResult> search_result;
        try {
            SearchControls searchCtrls = new SearchControls();
            searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchCtrls.setReturningAttributes(attributes);
            search_result = adContext.search(base, filter, searchCtrls);
            return search_result;
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object SetUserProp(String dn, String propName, String propValue) {
        return null;
    }

    public Object lockUser(String dn) {
        return null;
    }

    public Object unlockUser(String dn) {
        return null;
    }

    public List<User> searchUsers(String base) {
        List<User> users = new ArrayList<User>();

        NamingEnumeration<SearchResult> values = ADSearchUtils.searchDir(conn.getDirectoryContext(),
                base, User.getFilter(), User.getUserAttributeNames());
        users = parseResultForUserList(values);

        return users;
    }

    public User searchUser(String base, String dn) {
        NamingEnumeration<SearchResult> values = ADSearchUtils.searchDir(conn.getDirectoryContext(),
                base, User.getUserFilter(dn), User.getUserAttributeNames());
        if (values != null) {
            while (values.hasMoreElements()) {
                SearchResult result;
                try {
                    result = (SearchResult) values.next();
                    return parseResultForUser(result);
                } catch (NamingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private User parseResultForUser(SearchResult result) {
        User user = new User();
        Attributes attribs = result.getAttributes();
        if (null != attribs) {
            try {
                for (NamingEnumeration<?> ae = attribs.getAll(); ae.hasMoreElements();) {
                    Attribute atr = (Attribute) ae.next();
                    String attributeID = atr.getID();
                    for (Enumeration<?> vals = atr.getAll(); vals.hasMoreElements();) {
                        String attrValue = (String) vals.nextElement();
                        User.setProperty(user, attributeID, attrValue);
                    }
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return user;
    }

    private Group parseResultForGroup(SearchResult result) {
        Group group = new Group();
        Attributes attribs = result.getAttributes();
        if (null != attribs) {
            try {
                for (NamingEnumeration<?> ae = attribs.getAll(); ae.hasMoreElements();) {
                    Attribute atr = (Attribute) ae.next();
                    String attributeID = atr.getID();
                    for (Enumeration<?> vals = atr.getAll(); vals.hasMoreElements();) {
                        String attrValue = (String) vals.nextElement();
                        Group.setProperty(group, attributeID, attrValue);
                    }
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return group;
    }

    private List<User> parseResultForUserList(NamingEnumeration<SearchResult> values) {
        List<User> users = new ArrayList<>();
        if (values != null) {
            try {
                while (values.hasMoreElements()) {
                    SearchResult result = (SearchResult) values.next();
                    User user = parseResultForUser(result);
                    users.add(user);
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return users;
    }

    private List<Group> parseResultForGroupList(NamingEnumeration<SearchResult> values) {
        List<Group> groups = new ArrayList<>();
        if (values != null) {
            try {
                while (values.hasMoreElements()) {
                    SearchResult result = (SearchResult) values.next();
                    Group group = parseResultForGroup(result);
                    groups.add(group);
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return groups;
    }

    public List<String> searchUserGroups(String base, String dn) {
        List<String> groups = new ArrayList<>();
        String filter = "(member=" + dn + ")";
        filter = "(member:1.2.840.113556.1.4.1941:=" + dn + ")";
        NamingEnumeration<SearchResult> values =
                searchDir(conn.getDirectoryContext(), base, filter, new String[] {"member"});
        if (values != null) {
            try {
                while (values.hasMoreElements()) {
                    SearchResult result = (SearchResult) values.next();
                    Attributes attribs = result.getAttributes();
                    if (null != attribs) {
                        try {
                            for (NamingEnumeration<?> ae = attribs.getAll(); ae
                                    .hasMoreElements();) {
                                Attribute atr = (Attribute) ae.next();

                                for (Enumeration<?> vals = atr.getAll(); vals.hasMoreElements();) {
                                    String attrValue = (String) vals.nextElement();
                                    groups.add(attrValue);
                                }
                            }
                        } catch (NamingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return groups;
    }

    public List<Group> searchGroups(String base) {
        List<Group> groups = new ArrayList<Group>();

        NamingEnumeration<SearchResult> values =
                ADSearchUtils.searchDir(conn.getDirectoryContext(), base, Group.getFilter(), null);
        groups = parseResultForGroupList(values);

        return groups;
    }

    public Group searchGroup(String base, String groupName) {
        NamingEnumeration<SearchResult> values = ADSearchUtils.searchDir(conn.getDirectoryContext(),
                base, Group.getGroupFilter(groupName), null);

        if (values != null) {
            while (values.hasMoreElements()) {
                SearchResult result;
                try {
                    result = (SearchResult) values.next();
                    return parseResultForGroup(result);
                } catch (NamingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<String> searchGroupMembers(String base, String groupDN, boolean nested) {
        String filter = "(memberOf=" + groupDN + ")";
        if(nested) {
            filter = "(memberOf:1.2.840.113556.1.4.1941:="+ groupDN + ")";
        }
        NamingEnumeration<SearchResult> values =
                ADSearchUtils.searchDir(conn.getDirectoryContext(), base, filter, new String[] {"distinguishedName"});
        List<String> users = new ArrayList<>();
        if (values != null) {
            try {
                while (values.hasMoreElements()) {
                    SearchResult result = (SearchResult) values.next();
                    Attributes attribs = result.getAttributes();
                    if (null != attribs) {
                        try {
                            for (NamingEnumeration<?> ae = attribs.getAll(); ae
                                    .hasMoreElements();) {
                                Attribute atr = (Attribute) ae.next();

                                for (Enumeration<?> vals = atr.getAll(); vals.hasMoreElements();) {
                                    String attrValue = (String) vals.nextElement();
                                    users.add(attrValue);
                                }
                            }
                        } catch (NamingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return users;
    }

}
