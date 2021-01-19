package com.jlarsen.apidemo.controllers;

import com.jlarsen.apidemo.exceptions.UserException;
import com.jlarsen.apidemo.pojo.CodingNomadsResponse;
import com.jlarsen.apidemo.pojo.Error;
import com.jlarsen.apidemo.pojo.User;
import com.jlarsen.apidemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UsersController {

    final String url = "http://demo.codingnomads.co:8080/tasks_api/users/";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserService userService;

    @GetMapping
    public CodingNomadsResponse<ArrayList<User>> getUsers(@RequestParam(required = false, defaultValue = "-1") int count) {
        CodingNomadsResponse<ArrayList<User>> response = new CodingNomadsResponse<>();
        try {
            response.setData(userService.getUsers(count));
            response.setStatusCode(200);
        } catch (UserException e) {
            response.setError(new Error (e.getMessage()));
            response.setStatusCode(500);
        }
        return response;
    }

    @GetMapping("/{id}")
    public CodingNomadsResponse<User> getUser(@PathVariable int id) {
        CodingNomadsResponse<User> response = new CodingNomadsResponse<>();
        try {
            response.setData(userService.getUser(id));
            response.setStatusCode(200);
        } catch (UserException e) {
            response.setError(new Error (e.getMessage()));
            response.setStatusCode(500);
        }
        return response;
    }

    @GetMapping("/names")
    public ArrayList<String> getNames() {
        return userService.getNames();
    }

    @PostMapping //(value = "/create")
    public CodingNomadsResponse<User> postUser(@RequestBody User newUser) {
        CodingNomadsResponse<User> response = new CodingNomadsResponse<>();
        try {
            response.setData(userService.createUser(newUser));
            response.setStatusCode(200);
        } catch (UserException e) {
            response.setError(new Error (e.getMessage()));
            response.setStatusCode(500);
        }
        return response;
    }

    @PutMapping //(value = "/update")
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


}
