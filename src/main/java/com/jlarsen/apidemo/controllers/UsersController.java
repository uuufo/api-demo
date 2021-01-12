package com.jlarsen.apidemo.controllers;

import com.jlarsen.apidemo.pojo.CodingNomadsResponse;
import com.jlarsen.apidemo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/users")
public class UsersController {

    final String url = "http://demo.codingnomads.co:8080/tasks_api/users/";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public CodingNomadsResponse<ArrayList<User>, String> getUsers(@RequestParam(required = false, defaultValue = "-1") int count) {
        CodingNomadsResponse<ArrayList<LinkedHashMap>, String> response = restTemplate.getForObject(url, CodingNomadsResponse.class);
        CodingNomadsResponse<ArrayList<User>, String> properResponse = fixArray(response);
        if (count < 0) {
            return properResponse;
        }
        CodingNomadsResponse<ArrayList<User>, String> someUsers = new CodingNomadsResponse<>();
        someUsers.setData(new ArrayList<>());
        someUsers.setError(properResponse.getError());
        someUsers.setStatusCode(properResponse.getStatusCode());
        for (int i = 0; i < count; i++) {
            someUsers.getData().add(properResponse.getData().get(i));
        }
        return someUsers;
    }

    @GetMapping("/{id}")
    public CodingNomadsResponse<User, String> getUser(@PathVariable int id) {
        CodingNomadsResponse<LinkedHashMap, String> response = restTemplate.getForObject(url + id, CodingNomadsResponse.class);
        LinkedHashMap user = response.getData();
        CodingNomadsResponse<User, String> properResponse = new CodingNomadsResponse<>();
        properResponse.setData(new User((int) user.get("id"), user.get("first_name").toString(), user.get("last_name").toString(), user.get("email").toString(), (long) user.get("createdAt"), (long) user.get("updatedAt")));
        properResponse.setError(response.getError());
        properResponse.setStatusCode(response.getStatusCode());
        return (properResponse);
    }

    @GetMapping("/names")
    public ArrayList<String> getNames() {
        CodingNomadsResponse<ArrayList<LinkedHashMap>, String> response = restTemplate.getForObject(url, CodingNomadsResponse.class);
        CodingNomadsResponse<ArrayList<User>, String> responseList = fixArray(response);
        ArrayList<String> names = new ArrayList<>();
        for (User user : responseList.getData()) {
            names.add(user.getFirst_name() + " " + user.getLast_name());
        }
        return names;
    }

    @PostMapping(value = "/create")
    public CodingNomadsResponse<User, String> createUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        CodingNomadsResponse<LinkedHashMap, String>  response = restTemplate.postForObject(url, httpEntity, CodingNomadsResponse.class);
        LinkedHashMap responseUser = response.getData();
        CodingNomadsResponse<User, String> properResponse = new CodingNomadsResponse<>();
        //properResponse.setData(new User((int) user.get("id"), user.get("first_name").toString(), user.get("last_name").toString(), user.get("email").toString(), (long) user.get("createdAt"), (long) user.get("updatedAt")));
        properResponse.setError(response.getError());
        properResponse.setStatusCode(response.getStatusCode());
        return properResponse;
    }

    @PutMapping(value = "/update")
    public ResponseEntity<CodingNomadsResponse> updateUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        ResponseEntity<CodingNomadsResponse> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, CodingNomadsResponse.class);
        return response;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> removeUser(@PathVariable int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url + id, HttpMethod.DELETE, httpEntity, String.class);
        return response;
    }

    public CodingNomadsResponse<ArrayList<User>, String> fixArray(CodingNomadsResponse<ArrayList<LinkedHashMap>, String> response) {
        CodingNomadsResponse<ArrayList<User>, String> properResponse = new CodingNomadsResponse<>();
        properResponse.setData(new ArrayList<>());
        for (LinkedHashMap user : response.getData()) {
            properResponse.getData().add(new User((int) user.get("id"), user.get("first_name").toString(), user.get("last_name").toString(), user.get("email").toString(), (long) user.get("createdAt"), (long) user.get("updatedAt")));
        }
        properResponse.setError(response.getError());
        properResponse.setStatusCode(response.getStatusCode());
        return properResponse;
    }
}