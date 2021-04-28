package ru.devegang.dndmanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {
   @Expose
    private long id;

    @JsonIgnore
    private Character character;
    private long characterID;

    @Expose
    String name;

    @Expose
    String definition;

    @Expose
    double weight = 0;
}

