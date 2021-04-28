package ru.devegang.dndmanager.login_n_register;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.User;
import ru.devegang.dndmanager.networking.AuthService;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    EditText userLogin;
    EditText userName;
    EditText userPassword;

    Button SignUpButton;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        userLogin = (EditText)rootView.findViewById(R.id.edRegUserLogin);
        userName = (EditText)rootView.findViewById(R.id.etRegUserName);
        userPassword = (EditText) rootView.findViewById(R.id.etRegUserPassword);

        SignUpButton = (Button) rootView.findViewById(R.id.buttonRegSignUp);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NJ==TODO

                User user = new User();
                user.setLogin(userLogin.getText().toString());
                user.setName(userName.getText().toString());

                if(checkUser(user)) {

                    AuthService.getInstance()
                            .getRestUserAPI()
                            .registerUser(user)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.code() == HttpStatus.OK.value()) {

                                        AuthService.getInstance()
                                                .getRestUserAPI()
                                                .loginUser(user.getLogin())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        if(response.code() == HttpStatus.OK.value() && response.body()!= null){
                                                            SharedPreferences preferences = getActivity().getSharedPreferences("USER_INF",MODE_PRIVATE);
                                                            preferences.edit()
                                                                    .putLong("USER_ID",response.body().getId())
                                                                    .putString("USER_NAME",response.body().getName())
                                                                    .apply();

                                                            Navigation.findNavController(view).navigate(R.id.mainActivity);


                                                        } else {
                                                            Toast.makeText(view.getContext(),"Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        Toast.makeText(view.getContext(),"Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(view.getContext(),"Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(view.getContext(),"Incorrect Data",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    boolean checkUser (User user) {
        return user.getLogin() != null && user.getName() != null;
    }
}