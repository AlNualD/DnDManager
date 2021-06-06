package ru.devegang.dndmanager.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.Skill;
import ru.devegang.dndmanager.networking.NetworkService;

public class SkillsRecyclerViewAdapter extends RecyclerView.Adapter<SkillsViewHolder> {

    private List<Skill> skills;
    private  List<Skill> favorites;
    private LayoutInflater layoutInflater;
    private WeakReference<SkillsList> skillsListWeakReference;
    final int resID;
    private boolean favFlag = false;

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
        if(favFlag && favorites == null) {
            extractFavorites();
        }
    }

    public void extractFavorites() {
        favorites = new LinkedList<>();
        for (Skill skill : skills) {
            if(skill.getFavorite()) {
                favorites.add(skill);
            }
        }
    }

    public SkillsRecyclerViewAdapter(SkillsList skillsList, List<Skill> skills, int resID) {
        this.skills = skills;
        skillsListWeakReference = new WeakReference<>(skillsList);
        this.layoutInflater = LayoutInflater.from(skillsList.getContext());
        this.resID = resID;
    }


    @NonNull
    @Override
    public SkillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.card_skill,parent,false);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRecyclerItemClick((RecyclerView)parent,view);
            }
        });

        return new SkillsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsViewHolder holder, int position) {
        List<Skill> skills;
        if(favFlag) {
            skills = favorites;
        } else {
            skills = this.skills;
        }
        Skill skill = skills.get(position);
        holder.skillName.setText(skill.getName());
        holder.skillDescription.setText(skill.getDefinition());
        if(!skill.isTrait()) {
            holder.value.setText(String.valueOf(skill.getValue()));
        } else {
            holder.cardVal.setVisibility(View.GONE);
        }
        if(skill.getFavorite()) {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star);
        } else {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star_outline);
        }
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skill.setFavorite(!skill.getFavorite());
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .setFavoriteSkill(skill.getId(),skill.getFavorite())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                notifyItemChanged(position);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });



    }

    @Override
    public int getItemCount() {
        return favFlag ? favorites.size() : skills.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Skill skill;
        if(favFlag) {
            skill = this.favorites.get(itemPosition);
        }  else {
            skill = this.skills.get(itemPosition);
        }
        SkillsList skillsList = skillsListWeakReference.get();
        if(skillsList != null) {

            Toast.makeText(skillsList.getContext(), skill.getName(), Toast.LENGTH_LONG).show();


            Bundle bundle = new Bundle();
            bundle.putLong("SkillID",skill.getId());
            Navigation.findNavController(skillsList.getView()).navigate(resID, bundle);


        }



    }
}
