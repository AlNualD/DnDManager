package ru.devegang.dndmanager.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;

public class CharactersRecyclerViewAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    private List<Character> characters;
    //private Context context;
    private LayoutInflater layoutInflater;
    private WeakReference<CharactersList> charactersListWeakReference;

    public CharactersRecyclerViewAdapter(CharactersList charactersList, List<Character> characters) {
        charactersListWeakReference = new WeakReference<>(charactersList);
        //this.context = charactersList.getContext();
        this.characters = characters;
        this.layoutInflater = LayoutInflater.from(charactersList.getContext());
    }

    public void addCharacters(List<Character> characters) {
        this.characters.addAll(characters);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.card_character,parent,false);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecyclerItemClick((RecyclerView)parent, view);
            }
        });
        return new CharacterViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = this.characters.get(position);

        holder.characterName.setText(character.getName());
        holder.characterRace.setText(character.getRace());
        holder.characterClass.setText(character.getClassC());

    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
       Character character = this.characters.get(itemPosition);
       CharactersList charactersList = charactersListWeakReference.get();
       if(charactersList != null) {

           Toast.makeText(charactersList.getContext(), character.getName(), Toast.LENGTH_LONG).show();


//           SharedPreferences preferences = charactersList.getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
//           preferences.
           Bundle bundle = new Bundle();
           bundle.putLong("CharacterID",character.getId());
//           Navigation.findNavController(charactersList.getView()).navigate(R.id.action_open_character,bundle);
           Navigation.findNavController(charactersList.getView()).navigate(R.id.characterActivity,bundle);


       }



    }

}
