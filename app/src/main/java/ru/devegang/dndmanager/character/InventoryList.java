package ru.devegang.dndmanager.character;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.AttributeDialog;
import ru.devegang.dndmanager.dialogs.ItemDialog;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.entities.Item;
import ru.devegang.dndmanager.networking.NetworkService;

public class InventoryList extends Fragment {
    RecyclerView recyclerView;
    InventoryRecyclerViewAdapter adapter;
    List<Item> items;

    TextView weight;
    TextView money;
    ImageButton add;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory,container,false);
        getActivity().setTitle("Инвентарь");

        weight = (TextView) rootView.findViewById(R.id.tvInventoryWeight);
        money = (TextView) rootView.findViewById(R.id.tvItemsCount);

        add = (ImageButton) rootView.findViewById(R.id.ibInventoryAddItem);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        items = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvInventory);
        adapter = new InventoryRecyclerViewAdapter(this,items);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);
        if(id != -1) {

            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacterItems(id)
                    .enqueue(new Callback<List<Item>>() {
                        @Override
                        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                            if(response.isSuccessful()) {
                                items.addAll(response.body());
                                recyclerView.getAdapter().notifyDataSetChanged();

                                updateCommonInf();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Item>> call, Throwable t) {

                        }
                    });

            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacter(id)
                    .enqueue(new Callback<Character>() {
                        @Override
                        public void onResponse(Call<Character> call, Response<Character> response) {
                            if(response.isSuccessful()) {
                                Character character = response.body();
                                money.setText(String.valueOf(character.getMoney()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Character> call, Throwable t) {

                        }
                    });

            //updateCommonInf();


        }
        return rootView;
    }

    private void  updateCommonInf() {


        double curWeight = 0;
        for (Item item : items) {
            curWeight += item.getWeight();
        }
        weight.setText(String.valueOf(curWeight));
    }


    public void saveItem(Item item) {
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);

        if(item.getId() == 0) {
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .createItem(id,item)
                    .enqueue(new Callback<Item>() {
                        @Override
                        public void onResponse(Call<Item> call, Response<Item> response) {
                            if(response.isSuccessful()) {
                                items.add(response.body());
                                recyclerView.getAdapter().notifyDataSetChanged();
                                updateCommonInf();
                            }
                        }

                        @Override
                        public void onFailure(Call<Item> call, Throwable t) {

                        }
                    });
        } else {
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .updateItem(item.getId(),item)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                items.clear();
                                NetworkService.getInstance()
                                        .getRestCharacterAPIv2()
                                        .getCharacterItems(id)
                                        .enqueue(new Callback<List<Item>>() {
                                            @Override
                                            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                                                if(response.isSuccessful()) {
                                                    items.addAll(response.body());
                                                    adapter.notifyDataSetChanged();
                                                    updateCommonInf();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Item>> call, Throwable t) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
        }

    }

    public void openDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        ItemDialog itemDialog = new ItemDialog();
        itemDialog.setCancelable(true);
        itemDialog.setTargetFragment(this,111);
        itemDialog.show(fragmentManager,"");

    }
}
