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
import org.springframework.http.ResponseEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.User;
import ru.devegang.dndmanager.networking.AuthService;

import static android.content.Context.MODE_PRIVATE;


public class RegistrationFragment extends Fragment {

    EditText userLogin;
    EditText userName;
    EditText userPassword;

    Button SignUpButton;





    public RegistrationFragment() {
        // Required empty public constructor
    }


    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                User user = new User();
                user.setLogin(userLogin.getText().toString());
                user.setName(userName.getText().toString());

                if(checkUser(user)) {

                    AuthService.getInstance()
                            .getRestUserAPIv2()
                            .registerUser(user)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful() && response.body()!=null) {
                                        AuthSuccess(response.body());
                                    } else {
                                        Toast.makeText(getContext(),"Invalid data!", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(getContext(),"Invalid data!", Toast.LENGTH_SHORT).show();

                                }
                            });


                } else {
                    Toast.makeText(view.getContext(),"Incorrect Data",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    private void AuthSuccess(User user) {

        Toast.makeText(this.getContext(),user.getName() + " AuthSuccess!",Toast.LENGTH_SHORT).show();

        saveUser(user);

        Navigation.findNavController(this.getView()).navigate(R.id.mainActivity);

    }


    private void saveUser(User user) {
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_INF",MODE_PRIVATE);
        preferences.edit()
                .putLong("USER_ID",user.getId())
                .putString("USER_NAME",user.getName())
                .apply();

    }

    boolean checkUser (User user) {
        return user.getLogin() != null && !user.getLogin().isEmpty() && user.getName() != null && !user.getName().isEmpty();
    }
}