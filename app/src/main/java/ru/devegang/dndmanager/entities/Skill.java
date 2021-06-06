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
public class Skill {
    @Expose(serialize = false)
    private long id;

    @JsonIgnore
    private long characterID;

    int trainCoefficient;
    boolean canBeTrained;
    int value;
    String name;
    String definition;
    String description;
    boolean trait;
    @Expose(serialize = false)
    Attribute attribute;
    Boolean favorite = false;

}

