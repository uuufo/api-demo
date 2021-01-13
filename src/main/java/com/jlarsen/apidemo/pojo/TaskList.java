package com.jlarsen.apidemo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public class TaskList {

    private Task[] data;

    public TaskList() {
    }

    public Task[] getData() {
        return data;
    }

    public void setData(Task[] data) {
        this.data = data;
    }
}