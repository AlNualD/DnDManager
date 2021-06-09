package ru.devegang.dndmanager.entities;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;


@Getter
public class Attribute {
    @Expose(serialize = false)
    long id;
    String name;
    int amount;
    @Expose(serialize = false)
    int modification;
    boolean trainedSaveRoll;


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrainedSaveRoll(boolean trainedSaveRoll) {
        this.trainedSaveRoll = trainedSaveRoll;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setModification(int modification) {
        this.modification = modification;
    }

    public boolean checkAttribute() {
        return !name.isEmpty() && amount>0;
    }

    public static int countModification(int amount) {
        return (amount / 2) - 5;
    }


}
