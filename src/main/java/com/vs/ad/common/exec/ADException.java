package com.vs.ad.common.exec;

public class ADException extends Exception {

    private static final long serialVersionUID = -1984313866120180481L;
    private final ADErrorCode errorCode;

    @Deprecated
    public ADException() {
        super();
        errorCode = ADErrorCode.UNDEFINED;
    }

    @Deprecated
    public ADException(String message) {
        super(message);
        errorCode = ADErrorCode.UNDEFINED;

    }

    public ADException(String message, ADErrorCode code) {
        super(message);
        errorCode = code;
    }

    public ADException(String message, ADErrorCode code, Throwable e) {
        super(message, e);
        errorCode = code;
    }

    public ADErrorCode getErrorCode() {
        return errorCode;
    }


}
