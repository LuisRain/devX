package com.william.devx.core.exception;

/**
 * Created by sungang on 2018/3/20.
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException () {
        super();
    }

    public ResourceNotFoundException ( String message ) {
        super( message );
    }

    public ResourceNotFoundException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public ResourceNotFoundException ( Throwable cause ) {
        super( cause );
    }

    protected ResourceNotFoundException ( String message,
                                          Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
