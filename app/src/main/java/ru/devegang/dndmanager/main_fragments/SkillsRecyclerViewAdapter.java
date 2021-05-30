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
import java.util.List;

import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.Skill;

public class SkillsRecyclerViewAdapter extends RecyclerView.Adapter<SkillsViewHolder> {

    private List<Skill> skills;
    private LayoutInflater layoutInflater;
    private WeakReference<SkillsList> skillsListWeakReference;
    final int resID;
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
        Skill skill = this.skills.get(position);
        holder.skillName.setText(skill.getName());
        holder.skillDescription.setText(skill.getDefinition());
        if(!skill.isTrait()) {
            holder.value.setText(String.valueOf(skill.getValue()));
        } else {
            holder.cardVal.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Skill skill = this.skills.get(itemPosition);
        SkillsList skillsList = skillsListWeakReference.get();
        if(skillsList != null) {

            Toast.makeText(skillsList.getContext(), skill.getName(), Toast.LENGTH_LONG).show();


            Bundle bundle = new Bundle();
            bundle.putLong("SkillID",skill.getId());
            Navigation.findNavController(skillsList.getView()).navigate(resID, bundle);


        }



    }
}
