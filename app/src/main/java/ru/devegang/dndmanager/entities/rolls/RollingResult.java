package ru.devegang.dndmanager.entities.rolls;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RollingResult {
    int result;
    int[] rolls;
}
