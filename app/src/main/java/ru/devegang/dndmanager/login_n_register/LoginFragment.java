package ru.devegang.dndmanager.login_n_register;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.entities.User;
import ru.devegang.dndmanager.networking.AuthService;
import ru.devegang.dndmanager.networking.AuthStatus;
import ru.devegang.dndmanager.networking.UserAuth;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {




    private Button registrationButton;
    private Button loginButton;

    private EditText login;
    private EditText password;

    private SharedPreferences preferences;
    private Handler handler;
    private UserAuth userAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);



//        handler = new UserHandler(this);

//        userAuth = new UserAuth();

        login = (EditText) rootView.findViewById(R.id.etLoginLogin);
        password = (EditText) rootView.findViewById(R.id.etLoginPassword);


        registrationButton = (Button) rootView.findViewById(R.id.buttonRegistration);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.registrationFragment);
            }
        });

        loginButton = (Button) rootView.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userLogin = login.getText().toString();
                if(!userLogin.isEmpty()) {
                    User user = new User();
                    user.setLogin(userLogin);
                    AuthService.getInstance()
                            .getRestUserAPIv2()
                            .loginUser(userLogin)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful() && response.body() != null) {
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

//                    AuthService.getInstance()
//                            .getRestUserAPI()
//                            .loginUser(user.getLogin())
//                            .enqueue(new Callback<User>() {
//                                @Override
//                                public void onResponse(Call<User> call, Response<User> response) {
//                                    if(response.code() == HttpStatus.OK.value()) {
//                                        AuthSuccess(response.body());
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<User> call, Throwable t) {
//                                    Toast.makeText(getContext(),"Invalid data!", Toast.LENGTH_SHORT).show();
//
//                                }
//                            });
                    //userAuth.loginUser(user,handler);
                } else {
                    Toast.makeText(getContext(),"Invalid data!", Toast.LENGTH_SHORT).show();
                }

//                Navigation.findNavController(view).navigate(R.id.mainActivity);
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

        preferences = getActivity().getSharedPreferences("USER_INF",MODE_PRIVATE);
        preferences.edit()
                .putLong("USER_ID",user.getId())
                .putString("USER_NAME",user.getName())
                .apply();

    }

//
//    static class UserHandler extends Handler {
//        WeakReference<LoginFragment> loginFragmentWeakReference;
//        public UserHandler(LoginFragment fragment) {
//            loginFragmentWeakReference = new WeakReference<>(fragment);
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            LoginFragment fragment = loginFragmentWeakReference.get();
//            if(fragment != null) {
//
//                AuthStatus status = AuthStatus.getStatus(msg.what);
//                switch (status) {
//                    case STATUS_LOGIN_OK:
//                    case STATUS_SIGHUP_OK :
//                        fragment.AuthSuccess((User) msg.obj);
//                        //activity.startApp((User) msg.obj);
//                        break;
//                    case STATUS_LOGIN_FAIL:
//                    case STATUS_SIGNHUP_FAIL:
//                        Toast.makeText(fragment.getContext(),"Login Fail", Toast.LENGTH_SHORT).show();
//                        //Toast toast = Toast.makeText(activity.getApplicationContext(),"WRONG LOGIN TRY AGAIN",Toast.LENGTH_SHORT);
//                        //toast.show();
//                        break;
//                    case ERROR: System.out.println("ERROR");
//                        break;
//
//                }
//            }
//        }
//    }


}