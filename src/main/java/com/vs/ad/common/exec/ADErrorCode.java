package com.vs.ad.common.exec;

public enum ADErrorCode {

    UNDEFINED("Undefined"),
    EU0001("User not found"),
    EU0002("Users not found"),
    EU0003("User creation failed"),
    EU0004("User password updated failed"),
    EU0005("User deletion is failed"),
    EU0006("User attribute updation failed"),
    EU0007("Enabling user account failed"),
    EU0008("Disabling user account failed"),
    EU0009("User attribute retrieval failed"),
    EU0010("unlocking user account failed"),
    EU0011("locking user account failed"),
    
    OTHER("")
    ;

    private final String text;

    private ADErrorCode(final String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }
}
