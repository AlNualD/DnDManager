package ru.devegang.dndmanager.main_fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.devegang.dndmanager.R;


public class CharacterViewHolder extends RecyclerView.ViewHolder {
    TextView characterName;
    TextView characterRace;
    TextView characterClass;
    public CharacterViewHolder(@NonNull View itemView) {
        super(itemView);
        this.characterName = (TextView) itemView.findViewById(R.id.tvCharacterName);
        characterClass = (TextView) itemView.findViewById(R.id.tvCharacterClass);
        characterRace = (TextView) itemView.findViewById(R.id.tvCharacterRace);

    }
}
