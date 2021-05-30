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
public class Spells_slots {

    @Expose(serialize = false)
    long id;

    int lvl0 = 0;
    int lvl1 = 0;
    int lvl2 = 0;
    int lvl3 = 0;
    int lvl4 = 0;

    int lvl5 = 0;

    int lvl6 = 0;

    int lvl7 = 0;

    int lvl8 = 0;

    int lvl9 = 0;
}

