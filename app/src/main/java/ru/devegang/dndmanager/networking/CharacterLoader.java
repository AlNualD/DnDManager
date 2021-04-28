package ru.devegang.dndmanager.networking;

import android.os.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import ru.devegang.dndmanager.entities.Character;

public class CharacterLoader {

    final String baseUrl = "https://flexcharactersheet.herokuapp.com/api/v1/users/";
    final long UserID;
    RestTemplate restTemplate;

    public CharacterLoader(long userID) {
        UserID = userID;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public synchronized void loadAllCharacters(Handler handler) {
        ArrayList<Character> characters = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {



                    ResponseEntity<Character[]> responseEntity = restTemplate.getForEntity(baseUrl + "characters/" + UserID, Character[].class);
                    //ResponseEntity<ArrayList<Character>> responseEntity = restTemplate.getForEntity(baseUrl+"characters/" + UserID, characters.getClass());
                    //restTemplate.getMessageConverters().add()
                    if(responseEntity.getStatusCode() == HttpStatus.OK) {
                        for (Character character : responseEntity.getBody()) {
                            characters.add(character);
                        }
                        handler.sendMessage(handler.obtainMessage(CharacterLoaderStatus.OK.toInt(), characters));
                    } else {
                        handler.sendEmptyMessage(CharacterLoaderStatus.NOT_FOUND.toInt());

                    }


                } catch (HttpClientErrorException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(CharacterLoaderStatus.ERROR.toInt());

                }
            }
        });
        thread.start();
    }



}
