package com.vs.ad.vo;

import java.util.ArrayList;
import java.util.List;

public abstract class ActiveDirObject {
    protected static String[] defaultObjectClasses ={"top","person","organizationalPerson","user"};
    private String description;
    private String name;
    private String samAccountName;
    private String displayname;
    private String distinguishedName;
    private List<String> objectClasses;

    public static String[] getDefaultObjectClasses() {
        return defaultObjectClasses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSamAccountName() {
        return samAccountName;
    }

    public void setSamAccountName(String samAccountName) {
        this.samAccountName = samAccountName;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public List<String> getObjectClasses() {
        return objectClasses;
    }

    public void setObjectClasses(List<String> objectClasses) {
        this.objectClasses = objectClasses;
    }

    public void setObjectClasses(String objectClass) {
        if (objectClasses == null) {
            objectClasses = new ArrayList<>();
        }
        objectClasses.add(objectClass);
    }



}
