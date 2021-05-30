package ru.devegang.dndmanager.entities.rolls;

public enum Modes {
    DISADVANTAGE(-1),
    NORMAL(0),
    ADVANTAGE(1);

    int i;

    Modes(int i) {
        this.i = i;
    }

    public  static Modes  modByInt(int i) {
        for (Modes value : values()) {
            if(value.i == i) {return value;}
        }
        return null;
    }
}
