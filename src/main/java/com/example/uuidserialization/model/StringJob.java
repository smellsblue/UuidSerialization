package com.example.uuidserialization.model;

public class StringJob implements Job<String> {
    @Override
    public void process(String param) {
        System.out.println("Processing job: " + param);
    }
}
