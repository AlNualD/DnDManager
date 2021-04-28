package ru.devegang.dndmanager.networking;

public enum AuthStatus {
    STATUS_LOGIN_OK(1),
    STATUS_LOGIN_FAIL(0),
    STATUS_SIGHUP_OK(3),
    STATUS_SIGNHUP_FAIL(2),
    ERROR(-1);

    private int code;

    AuthStatus(int code) {
        this.code = code;
    }

    public static AuthStatus getStatus(int code) {
        for(AuthStatus status : AuthStatus.values()) {
            if(status.toInt() == code) {
                return status;
            }
        }
        return ERROR;
    }
    public int toInt(){
        return this.code;
    }



}
