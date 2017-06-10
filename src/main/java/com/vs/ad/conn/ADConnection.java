package com.vs.ad.conn;

import javax.naming.directory.DirContext;

public interface ADConnection {

    DirContext getDirectoryContext();
    String getSearchBase();
    void closeConnection();
    String getDomainName();
}
