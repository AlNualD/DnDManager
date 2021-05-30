package ru.devegang.dndmanager.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Expose(serialize = false)
    private long id;
    String name;
    String login;
    @Expose(serialize = false)
    int character_count = 0;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
//    public Set<Character> characters = new HashSet<>();
//    @OneToMany(mappedBy = "user_id", fetch = FetchType.LAZY)
//    private Collection<Character> characters;



    @JsonIgnore
    private List<Character> characters = new ArrayList<>();


    @Override
    public String toString() {
        //return super.toString();
        return "{user " + id + " " + name + "}";
    }
}
