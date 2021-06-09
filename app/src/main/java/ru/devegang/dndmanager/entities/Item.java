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
   @Expose(serialize = false)
    private long id;

    String name;
    String definition;
    double weight = 0;
    String formula;
    Boolean favorite = false;
}

