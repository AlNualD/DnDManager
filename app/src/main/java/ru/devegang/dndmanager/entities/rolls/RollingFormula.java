package ru.devegang.dndmanager.entities.rolls;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RollingFormula {
    Modes mode;
    int dicesAmount;
    int dice;
    int modification = 0;


    public String getReadable() {
        String modif = modification>0 ? "+" : "";
        return mode.toString() + " " + dicesAmount + "d"+dice + modif + modification;
    }


    @NonNull
    @Override
    public String toString() {
        return mode.i + ";" + dicesAmount +";" + dice +";" + modification;
    }


    public static boolean checkStringFormula(String formula) {
        Pattern pattern = Pattern.compile("^(0|-?1);\\d+;\\d+;-?\\d+");
        Matcher matcher = pattern.matcher(formula);
        return matcher.matches();
    }

    public static RollingFormula gerRollingFormula(String formula) {
        if (checkStringFormula(formula)) {
            String[] values = formula.split(";");
            return new RollingFormula(Modes.modByInt(Integer.parseInt(values[0])),Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]));
        }
        return null;



    }
}
