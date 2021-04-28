package ru.devegang.dndmanager.networking;

import android.os.Handler;
import android.os.Message;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ru.devegang.dndmanager.entities.User;


public class UserAuth {

    final String baseUrl =  "https://flexcharactersheet.herokuapp.com/api/v1/users/";
    String urlModif;
    RestTemplate restTemplate;

    public UserAuth() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }
    public synchronized void loginUser(User user, Handler handler) {
        Thread thread = new Thread(new Runnable() {
            Message msg;
            @Override
            public void run() {
                try {
                    ResponseEntity<User> responseEntity = restTemplate.getForEntity(baseUrl + "login/" + user.getLogin(), User.class);

                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        User u = responseEntity.getBody();

                        msg = handler.obtainMessage(AuthStatus.STATUS_LOGIN_OK.toInt(), u);
                        handler.sendMessage(msg);


                    } else {
                        handler.sendEmptyMessage(AuthStatus.STATUS_LOGIN_FAIL.toInt());
                    }
                } catch (HttpClientErrorException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(AuthStatus.STATUS_LOGIN_FAIL.toInt());
                }
            }
        });
        thread.start();

    }

    public synchronized void registerUser(User user, Handler handler) {
        Thread thread = new Thread(new Runnable() {
            Message msg;
            @Override
            public void run() {
                HttpEntity<User> request = new HttpEntity<>(user);
                try {
                    ResponseEntity<User> responseEntity = restTemplate.postForEntity(baseUrl, request, User.class);
                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        msg = handler.obtainMessage(AuthStatus.STATUS_SIGHUP_OK.toInt(), user);
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(AuthStatus.STATUS_SIGNHUP_FAIL.toInt());
                    }
                } catch (HttpClientErrorException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(AuthStatus.STATUS_LOGIN_FAIL.toInt());
                }
            }
        });
        thread.start();
    }


}
