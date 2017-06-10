package com.vs.ad;

import java.util.List;

import com.vs.ad.common.Utils;
import com.vs.ad.conn.ADConnection;
import com.vs.ad.conn.impl.ADConnectionImpl;
import com.vs.ad.conn.impl.ADSearchUtils;
import com.vs.ad.vo.Group;

@SuppressWarnings("unused")
public class ADSearchUtilsTest {
    static {
        System.setProperty("javax.net.ssl.trustStore", "E:\\Srinivas_Vutukoori\\cacerts");
    }
    final static String base = "DC=novelirs,DC=com";
    static ADConnection con = new ADConnectionImpl();
    static ADSearchUtils util = new ADSearchUtils(con);

    public static void main(String[] args) {


        Object obj = util.searchGroupMembers(base, "CN=Enterprise Admins,CN=Users,DC=novelirs,DC=com", true);
        //List<Group> groups = util.searchGroups(base);
        System.out.println(Utils.getJson(obj));

        con.closeConnection();

    }


    private static void getGroups() {
        List<String> groups =
                util.searchUserGroups(base, "CN=Administrator,CN=Users,DC=novelirs,DC=com");
        System.out.println(groups);
    }


}
