package com.jlarsen.apidemo.services;

import com.jlarsen.apidemo.exceptions.UserException;
import com.jlarsen.apidemo.pojo.CodingNomadsResponse;
import com.jlarsen.apidemo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    final String url = "http://demo.codingnomads.co:8080/tasks_api/users/";

    public ArrayList<User> getUsers(int count) throws UserException {
        CodingNomadsResponse<ArrayList<LinkedHashMap>> response = restTemplate.getForObject(url, CodingNomadsResponse.class);
        CodingNomadsResponse<ArrayList<User>> properResponse = fixArray(response);
        ArrayList<User> users = properResponse.getData();
        if (users == null) {
            throw new UserException("No users found!");
        }
        if (count < 0) {
            return users;
        }
        ArrayList<User> someUsers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            someUsers.add(properResponse.getData().get(i));
        }
        return someUsers;
    }

    public User getUser(int id) throws UserException {
        CodingNomadsResponse<LinkedHashMap> response = restTemplate.getForObject(url + id, CodingNomadsResponse.class);
        LinkedHashMap user = response.getData();
        if (user != null) {
            return fixUser(user);
        } else {
            throw new UserException("User with id: " + id + " does not exist");
        }
    }

    public User createUser(User newUser) throws UserException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(newUser.toJSON(), headers);
        CodingNomadsResponse<LinkedHashMap> response = restTemplate.postForObject(url, httpEntity, CodingNomadsResponse.class);
        LinkedHashMap user = response.getData();
        if (user != null) {
            return fixUser(user);
        } else {
            String reason = response.getError().getMessage();
            reason = reason.substring(reason.lastIndexOf(';') + 1);
            throw new UserException("Unable to create user - " + reason);
        }
    }

    public ArrayList<String> getNames() {
        CodingNomadsResponse<ArrayList<LinkedHashMap>> response = restTemplate.getForObject(url, CodingNomadsResponse.class);
        CodingNomadsResponse<ArrayList<User>> responseList = fixArray(response);
        ArrayList<String> names = new ArrayList<>();
        for (User user : responseList.getData()) {
            names.add(user.getFirst_name() + " " + user.getLast_name());
        }
        return names;
    }

    public User fixUser(LinkedHashMap user) {
        return new User((int) user.get("id"), user.get("first_name").toString(), user.get("last_name").toString(),
                user.get("email").toString(), (long) user.get("createdAt"), (long) user.get("updatedAt"));
    }

    public CodingNomadsResponse<ArrayList<User>> fixArray(CodingNomadsResponse<ArrayList<LinkedHashMap>> response) {
        CodingNomadsResponse<ArrayList<User>> properResponse = new CodingNomadsResponse<>();
        properResponse.setData(new ArrayList<>());
        for (LinkedHashMap user : response.getData()) {
            properResponse.getData().add(new User((int) user.get("id"), user.get("first_name").toString(), user.get("last_name").toString(), user.get("email").toString(), (long) user.get("createdAt"), (long) user.get("updatedAt")));
        }
        properResponse.setError(response.getError());
        properResponse.setStatusCode(response.getStatusCode());
        return properResponse;
    }
}
