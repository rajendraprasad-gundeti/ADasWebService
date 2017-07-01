package com.vs.ad.service.impl;

public enum ADUserFlags {
    // https://msdn.microsoft.com/en-us/library/aa772300(v=vs.85).aspx
    DISABLED("ADS_UF_ACCOUNTDISABLE", 0x02), // 2
    LOCKED("ADS_UF_LOCKOUT", 0x10), // 16

    PWD_EXPIRED("ADS_UF_PASSWORD_EXPIRED", 0x800000), // 8388608

    OTHER("", -1);

    private final int code;
    private final String type;

    private ADUserFlags(String type, int code) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + ":" + code;
    }
}
