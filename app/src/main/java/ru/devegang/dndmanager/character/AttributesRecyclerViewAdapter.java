package ru.devegang.dndmanager.character;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.AttributeDialog;
import ru.devegang.dndmanager.dialogs.ItemDialog;
import ru.devegang.dndmanager.dialogs.RollResultDialog;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.rolls.Modes;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.entities.rolls.RollingResult;
import ru.devegang.dndmanager.main_fragments.CharactersList;
import ru.devegang.dndmanager.networking.NetworkService;

public class AttributesRecyclerViewAdapter extends  RecyclerView.Adapter<AttributeViewHolder> {

    private List<Attribute> attributes;
    private LayoutInflater layoutInflater;

    private WeakReference<CharacterInfoFull> characterInfoFullWeakReference;
    private WeakReference<AttributesEdit> attributesEditWeakReference;


    public AttributesRecyclerViewAdapter(CharacterInfoFull characterInfoFull, List<Attribute> attributes) {
        this.attributes = attributes;
        this.characterInfoFullWeakReference = new WeakReference<>(characterInfoFull);
        this.layoutInflater = LayoutInflater.from(characterInfoFull.getContext());

    }

    public AttributesRecyclerViewAdapter(AttributesEdit attributesEdit, List<Attribute> attributes) {
        this.attributes = attributes;
        this.attributesEditWeakReference = new WeakReference<>(attributesEdit);
        this.layoutInflater = LayoutInflater.from(attributesEdit.getContext());
    }


    public void addAttributes(List<Attribute> attributes) {
        this.attributes.addAll(attributes);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttributeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.card_attrubute, parent, false);

//        item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handleRecyclerItemClick((RecyclerView) parent, view);
//            }
//        });
//
//
//        item.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                handleRecyclerItemClick((RecyclerView) parent, view);
//                return true;
//            }
//        });

        return new AttributeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeViewHolder holder, int position) {
        Attribute attribute = this.attributes.get(position);

        holder.AttrName.setText(attribute.getName());
        holder.AttrValue.setText(String.valueOf(attribute.getAmount()));

        if(attribute.getModification()<=0) {
            holder.AttrModif.setText(String.valueOf(attribute.getModification()));
        } else {
            String modif = "+" + attribute.getModification();
            holder.AttrModif.setText(modif);
        }


        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (attributesEditWeakReference != null){
                    PopupMenu popupMenu = new PopupMenu(layoutInflater.getContext(), holder.card);
                    popupMenu.inflate(R.menu.edit_delete_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.basicEditItem:
                                    if (attributesEditWeakReference != null) {
                                        AttributesEdit attributesEdit = attributesEditWeakReference.get();
                                        if (attributesEdit != null) {
                                            attributes.remove(attribute.getId());
                                            notifyDataSetChanged();

                                            FragmentManager fragmentManager = attributesEdit.getParentFragmentManager();
                                            AttributeDialog attributeDialog = new AttributeDialog();
                                            attributeDialog.setCancelable(true);
                                            attributeDialog.setTargetFragment(attributesEdit, 111);
                                            attributeDialog.show(fragmentManager, String.valueOf(attribute.getId()));

                                        }
                                    }
                                    break;
                                case R.id.basicDeleteItem:
                                    NetworkService.getInstance()
                                            .getRestCharacterAPIv2()
                                            .deleteAttribute(attribute.getId())
                                            .enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if (response.isSuccessful()) {
                                                        attributes.remove(attribute.getId());
                                                        notifyDataSetChanged();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }

                if(characterInfoFullWeakReference != null ) {
                    CharacterInfoFull characterInfoFull = characterInfoFullWeakReference.get();
                    if(characterInfoFull != null) {
                        int modif = attribute.getModification();
                        if(attribute.isTrainedSaveRoll()) {
                            SharedPreferences preferences = characterInfoFull.getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
                            modif += preferences.getInt("CharacterProfBonus", 0);
                        }
                        RollingFormula rollingFormula = new RollingFormula(Modes.NORMAL,1,20,modif);

                        PopupMenu popupMenu = new PopupMenu(layoutInflater.getContext(), holder.card);
                        popupMenu.inflate(R.menu.rolling_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.menuRollNegative:
                                        rollingFormula.setMode(Modes.DISADVANTAGE);
                                        break;
                                    case R.id.menuRollNormal:
                                        rollingFormula.setMode(Modes.NORMAL);
                                        break;
                                    case R.id.menuRollPositive:
                                        rollingFormula.setMode(Modes.ADVANTAGE);
                                        break;

                                }
                                makeRoll(rollingFormula);
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                }


                return false;

            }
        });

    }

    private void makeRoll(RollingFormula rollingFormula) {
        NetworkService.getInstance()
                .getRestRollingAPIv2()
                .simpleRoll(rollingFormula.toString())
                .enqueue(new Callback<RollingResult>() {
                    @Override
                    public void onResponse(Call<RollingResult> call, Response<RollingResult> response) {
                        if(response.isSuccessful()) showDialog(response.body().getResult());
                    }

                    @Override
                    public void onFailure(Call<RollingResult> call, Throwable t) {

                    }
                });
    }

    private void showDialog(int res) {
        if(characterInfoFullWeakReference!= null) {
            CharacterInfoFull characterInfoFull = characterInfoFullWeakReference.get();
            if(characterInfoFull != null) {
                FragmentManager fragmentManager = characterInfoFull.getParentFragmentManager();
                RollResultDialog resultDialog = new RollResultDialog();
                resultDialog.setCancelable(true);
                resultDialog.setTargetFragment(characterInfoFull,111);
                resultDialog.show(fragmentManager,String.valueOf(res));
            }
        }
    }

    @Override
    public int getItemCount() {
        return attributes.size();
    }





}
