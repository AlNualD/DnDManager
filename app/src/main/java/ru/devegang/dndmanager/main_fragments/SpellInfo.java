package ru.devegang.dndmanager.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.networking.NetworkService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellInfo extends Fragment {


    private TextView spellName;
    private TextView spellDefinition;
    private TextView spellDescription;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpellInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpellInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static SpellInfo newInstance(String param1, String param2) {
        SpellInfo fragment = new SpellInfo();
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
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_spell_info, container, false);

        spellName = (TextView) rootView.findViewById(R.id.tvInfoSpellName);
        spellDefinition = (TextView) rootView.findViewById(R.id.tvInfoSpellDefinition);
        spellDescription = (TextView) rootView.findViewById(R.id.tvInfoSpellDescription);

        long id = getArguments().getLong("SpellID");
        if(id>0) {
            NetworkService.getInstance()
                    .getRestCharacterAPI()
                    .getSpell(id)
                    .enqueue(new Callback<Spell>() {
                        @Override
                        public void onResponse(Call<Spell> call, Response<Spell> response) {
                            setSpellInf(response.body());
                        }

                        @Override
                        public void onFailure(Call<Spell> call, Throwable t) {
                            Toast.makeText(getContext(),"SmthWrong",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.spellsList);
                        }
                    });
        }

        return rootView;
    }

    private void setSpellInf(Spell spell) {
        spellName.setText(spell.getName());
        spellDefinition.setText(spell.getDefinition());
        spellDescription.setText(spell.getDescription());
    }
}