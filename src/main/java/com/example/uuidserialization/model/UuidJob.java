package com.example.uuidserialization.model;

import java.util.UUID;

public class UuidJob implements Job<UUID> {
    @Override
    public void process(UUID param) {
        System.out.println("Processing job: " + param);
    }
}
