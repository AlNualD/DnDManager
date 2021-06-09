package ru.devegang.dndmanager.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Character {

    @Expose(serialize = false)
    private long id;

//    @Column(name = "user_id")
//    private long user_id;


    String name;
    String classC;
    String race;
    int lvl;
    int healthDice = 1;
    int hp_cur;
    int hp_max = 1;
    String alignment;
    int spells_total;
    double money;
    int profBonus = 1;
    String description = "";
    String url;


    @JsonIgnore
    private User user;

    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    @JsonIgnore
    private List<Spell> spells = new ArrayList<>();
    @JsonIgnore
    private List<Item> inventory = new ArrayList<>();

    @JsonIgnore
    private Spells_slots slots;
//
//    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinTable(name = "users")
////    @JoinColumn (name = "users_id", referencedColumnName = "id")
//    private User user;


    @Override
    public String toString() {
        //return super.toString();
        return "{character " + id + " uid " + user.getId() + " name:" + name + "}";
    }
}