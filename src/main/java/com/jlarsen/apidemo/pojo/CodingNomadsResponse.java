package com.jlarsen.apidemo.pojo;

public class CodingNomadsResponse<T> {

    private T data;
    private Error error;
    private int statusCode;

    public CodingNomadsResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
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
