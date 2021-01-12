package com.jlarsen.apidemo.pojo;

public class CodingNomadsResponse<T, E> {

    private T data;
    private E error;
    private int statusCode;

    public CodingNomadsResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public E getError() {
        return error;
    }

    public void setError(E error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "CodingNomadsResponse{" +
                "data=" + data +
                ", error='" + error + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
