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

    @SerializedName("id")
    @Expose
    private long id;

//    @Column(name = "user_id")
//    private long user_id;


    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("classC")
    @Expose
    String classC;

    @SerializedName("race")
    @Expose
    String race;

    @SerializedName("lvl")
    @Expose
    int lvl;


    @SerializedName("hp_cur")
    @Expose
    int hp_cur;
    @SerializedName("hp_max")
    @Expose
    int hp_max;

    @SerializedName("alignment")
    @Expose
    String alignment;

    @SerializedName("spells_total")
    @Expose
    int spells_total;

    @SerializedName("money")
    @Expose
    double money;


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