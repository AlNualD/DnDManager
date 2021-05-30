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
public class Spell {

    @Expose(serialize = false)
    private long id;

    @JsonIgnore
    private long characterID;

    String name;
    String definition;
    String description;
    String formula;
}
