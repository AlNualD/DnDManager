package ru.devegang.dndmanager.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.ChooseTypeOfCreationDialog;
import ru.devegang.dndmanager.entities.Character;
import ru.devegang.dndmanager.networking.CharacterLoader;
import ru.devegang.dndmanager.networking.CharacterLoaderStatus;
import ru.devegang.dndmanager.networking.NetworkService;
import ru.devegang.dndmanager.networking.RestCharacterAPI;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharactersList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharactersList extends Fragment {

    private RecyclerView recyclerView;
    private CharactersRecyclerViewAdapter adapter;
    private FloatingActionButton addButton;
    private SharedPreferences preferences;

//    CharactersHandler handler;
    CharacterLoader loader;



    List<Character> characters;





    public CharactersList() {
        // Required empty public constructor
    }



    public static CharactersList newInstance(String param1, String param2) {
        CharactersList fragment = new CharactersList();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_characters_list, container, false);
        getActivity().setTitle("???????????? ????????????????????");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.rvCharactersList);
        characters = new ArrayList<>();  //getExampleCharacters();
        adapter = new CharactersRecyclerViewAdapter(this,characters);
        recyclerView.setAdapter(adapter);

        addButton = (FloatingActionButton)rootView.findViewById(R.id.addButton);
//        handler = new CharactersHandler(this);




        preferences = getActivity().getSharedPreferences("USER_INF", MODE_PRIVATE);
        long id = preferences.getLong("USER_ID",-1);
        if (id != -1) {
//            loader = new CharacterLoader(id);
//            loader.loadAllCharacters(handler);

            NetworkService.getInstance().getRestCharacterAPIv2().getUserCharacters(id)
//                    .getRestCharacterAPI()
//                    .getCharactersList(id)
                    .enqueue(new Callback<List<Character>>() {
                        @Override
                        public void onResponse(Call<List<Character>> call, Response<List<Character>> response) {
                            List<Character>  responseList = response.body();
                            if(responseList != null && !responseList.isEmpty()) {
                                Collections.reverse(responseList);
                                characters.addAll(responseList);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(),"???????? ?????????? ??????????... ???????????????? ??????????????????!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Character>> call, Throwable t) {
                            Toast.makeText(getContext(),"Error in loading", Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            System.out.print("CANT LOAD REFS!!!!!!!!!!");
        }

        //loader = new CharacterLoader();
        //loader.loadAllCharacters(handler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager manager = getChildFragmentManager();
//                ChooseTypeOfCreationDialog dialog = new ChooseTypeOfCreationDialog();
//                dialog.show(manager,"ccDialog");

                Bundle bundle = new Bundle();
                bundle.putLong("CharacterID", -1);
                Navigation.findNavController(view).navigate(R.id.characterActivity);
                //Navigation.findNavController(view).navigate(R.id.characterInfoEdit);
            }
        });
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://flexcharactersheet.herokuapp.com/api") //?????????????? ?????????? ????????????
//                .addConverterFactory(GsonConverterFactory.create()) //??????????????????, ?????????????????????? ?????? ???????????????????????????? JSON'?? ?? ??????????????
//                .build();
//         restCharacterAPI = retrofit.create(RestCharacterAPI.class); //?????????????? ????????????, ?????? ???????????? ???????????????? ?????????? ?????????????????? ??????????????
//


        preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        preferences.edit().clear().apply();

        return rootView;
    }



    private void upgradeCharactersList(List<Character> characters){
        adapter.addCharacters(characters);
    }

//    static class CharactersHandler extends Handler {
//        WeakReference<CharactersList> charactersFragmentWeakReference;
//        public CharactersHandler(CharactersList fragment) {
//            charactersFragmentWeakReference = new WeakReference<>(fragment);
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            CharactersList charactersFragment = charactersFragmentWeakReference.get();
//            if(charactersFragment != null) {
//                CharacterLoaderStatus status = CharacterLoaderStatus.getStatus(msg.what);
//                switch (status) {
//                    case OK: charactersFragment.upgradeCharactersList((ArrayList<Character>)msg.obj);
//                        break;
//                    case NOT_FOUND:
//                    case ERROR:
//                        System.out.print("EMPTY");
//                }
//            }
//
//        }
//    }





}