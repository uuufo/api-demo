package com.jlarsen.apidemo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlarsen.apidemo.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    final String url = "http://demo.codingnomads.co:8080/tasks_api/tasks/";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public MultipleTaskResponse getTasks() {
        //TaskList response = restTemplate.getForObject(url, TaskList.class);
        //ResponseEntity<TaskList> response = restTemplate.getForEntity(url, TaskList.class);
        return restTemplate.getForObject(url, MultipleTaskResponse.class);
    }

    @GetMapping("/{id}")
    public SingleTaskResponse getTask(@PathVariable int id) {
        return restTemplate.getForObject(url + id, SingleTaskResponse.class);
    }

    @GetMapping("/names")
    public ArrayList<String> getNames() {
        MultipleTaskResponse response = restTemplate.getForObject(url, MultipleTaskResponse.class);
        ArrayList<String> names = new ArrayList<>();
        for (Task task : response.getData()) {
            names.add(task.getName());
        }
        return names;
    }

    @PostMapping(value = "/create")
    public SingleTaskResponse createTask(@RequestBody Task newTask) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(newTask), headers);
        return restTemplate.postForObject(url, httpEntity, SingleTaskResponse.class);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<CodingNomadsResponse> updateTask(@RequestBody Task task) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> httpEntity = new HttpEntity<>(mapper.writeValueAsString(task), headers);
        return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, CodingNomadsResponse.class);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> removeTask(@PathVariable int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Task> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url + id, HttpMethod.DELETE, httpEntity, String.class);
    }
}
