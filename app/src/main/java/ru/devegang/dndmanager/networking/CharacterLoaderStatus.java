package ru.devegang.dndmanager.networking;

public enum CharacterLoaderStatus {
    ERROR (-1),
    NOT_FOUND(1),
    OK(2),
    ADDED(3);

    private int code;

    CharacterLoaderStatus(int code) {
        this.code = code;
    }


    public static CharacterLoaderStatus getStatus(int code) {
        for(CharacterLoaderStatus status : CharacterLoaderStatus.values()) {
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

