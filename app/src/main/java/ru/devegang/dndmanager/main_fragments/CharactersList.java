package ru.devegang.dndmanager.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.devegang.dndmanager.R;
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

    CharactersHandler handler;
    CharacterLoader loader;


//    private Retrofit retrofit;
//    private static RestCharacterAPI restCharacterAPI;

    List<Character> characters;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public CharactersList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CharactersList.
     */
    // TODO: Rename and change types and number of parameters
    public static CharactersList newInstance(String param1, String param2) {
        CharactersList fragment = new CharactersList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_characters_list, container, false);
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

            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getCharactersList(id)
                    .enqueue(new Callback<List<Character>>() {
                        @Override
                        public void onResponse(Call<List<Character>> call, Response<List<Character>> response) {
                            List<Character>  responseList = response.body();
                            characters.addAll(responseList);
                            recyclerView.getAdapter().notifyDataSetChanged();
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
                Navigation.findNavController(view).navigate(R.id.characterInfoEdit);
            }
        });
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://flexcharactersheet.herokuapp.com/api") //Базовая часть адреса
//                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
//                .build();
//         restCharacterAPI = retrofit.create(RestCharacterAPI.class); //Создаем объект, при помощи которого будем выполнять запросы
//


        preferences = getActivity().getSharedPreferences("CHARACTER", MODE_PRIVATE);
        preferences.edit().clear().apply();

        return rootView;
    }


    private List<Character> getExampleCharacters() {
        List<Character> characters = new ArrayList<>();
        Character c1 = new Character();
        c1.setName("David");
        c1.setClassC("Paladin");
        c1.setRace("Human");
        Character c2 = new Character();
        c2.setName("Laura");
        c2.setRace("Elf");
        c2.setClassC("Wizard");
        Character c3 = new Character();
        c3.setName("Richard");
        c3.setRace("Half-elf");
        c3.setClassC("Druid");

        characters.add(c1);
        characters.add(c2);
        characters.add(c3);

        return characters;
    }

    private void upgradeCharactersList(List<Character> characters){
        adapter.addCharacters(characters);
    }

    static class CharactersHandler extends Handler {
        WeakReference<CharactersList> charactersFragmentWeakReference;
        public CharactersHandler(CharactersList fragment) {
            charactersFragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            CharactersList charactersFragment = charactersFragmentWeakReference.get();
            if(charactersFragment != null) {
                CharacterLoaderStatus status = CharacterLoaderStatus.getStatus(msg.what);
                switch (status) {
                    case OK: charactersFragment.upgradeCharactersList((ArrayList<Character>)msg.obj);
                        break;
                    case NOT_FOUND:
                    case ERROR:
                        System.out.print("EMPTY");
                }
            }

        }
    }

}