package ru.devegang.dndmanager.character;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.AttributeDialog;
import ru.devegang.dndmanager.entities.Attribute;
import ru.devegang.dndmanager.networking.NetworkService;

import static android.content.Context.MODE_PRIVATE;

public class AttributesEdit extends Fragment {

    public AttributesEdit() {

    }
    RecyclerView recyclerViewAttributes;
    Button bAdd;
    Button bSave;

    AttributesRecyclerViewAdapter adapter;
    List<Attribute> attributes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_attributes,container,false);
        getActivity().setTitle("Редактирование атрибутов");

        recyclerViewAttributes = (RecyclerView) rootView.findViewById(R.id.rvChangeAttributes);
        bAdd = (Button) rootView.findViewById(R.id.bAddAttribute);
        bSave = (Button) rootView.findViewById(R.id.bSaveAttributes);

        attributes = new LinkedList<>();
        adapter = new AttributesRecyclerViewAdapter(this,attributes);
        recyclerViewAttributes.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewAttributes.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);

        if(id != -1) {
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .getCharacterAttributes(id)
                    .enqueue(new Callback<List<Attribute>>() {
                        @Override
                        public void onResponse(Call<List<Attribute>> call, Response<List<Attribute>> response) {
                            if(response.isSuccessful()) {
                                attributes.addAll(response.body());
                                recyclerViewAttributes.getAdapter().notifyDataSetChanged();
                                SharedPreferences preferencesAttr = getActivity().getSharedPreferences("BASIC_COST",MODE_PRIVATE);
                                if(!attributes.isEmpty() && preferencesAttr.getInt(String.valueOf(id),-2) < 0) {
                                    int sum = 0;
                                    for (Attribute attribute : attributes) {
                                        sum += attribute.getAmount();
                                    }
                                    preferencesAttr.edit().putInt(String.valueOf(id),sum).apply();
                                }
                            } else {
                                Toast.makeText(getContext(),"Opps< smth went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Attribute>> call, Throwable t) {
                            Toast.makeText(getContext(),"Opps< smth went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });


        return rootView;
    }

    private void openDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        AttributeDialog attributeDialog = new AttributeDialog();
        attributeDialog.setCancelable(true);
        attributeDialog.setTargetFragment(this,ADD_ATTR);
        attributeDialog.show(fragmentManager,"");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void saveAttribute(Attribute attribute) {
        SharedPreferences preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        long id = preferences.getLong("CharacterID", -1);
        if(id != -1) {
            NetworkService.getInstance()
                    .getRestCharacterAPIv2()
                    .createAttribute(id,attribute)
                    .enqueue(new Callback<Attribute>() {
                        @Override
                        public void onResponse(Call<Attribute> call, Response<Attribute> response) {
                            if(response.isSuccessful()) {
                                attributes.add(response.body());
                                recyclerViewAttributes.getAdapter().notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<Attribute> call, Throwable t) {
                            Toast.makeText(getContext(),"Opps< smth went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private final int ADD_ATTR = 111;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_ATTR:

                    break;

            }
        }
    }
}
