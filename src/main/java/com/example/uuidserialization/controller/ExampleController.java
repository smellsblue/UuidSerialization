package com.example.uuidserialization.controller;

import com.example.uuidserialization.model.Jobs;
import com.example.uuidserialization.model.StringJob;
import com.example.uuidserialization.model.UuidJob;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ExampleController {
    private final Jobs jobs;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String queueJob() {
        jobs.queue(StringJob.class, "Hello World");
        jobs.queue(UuidJob.class, UUID.randomUUID());
        jobs.queueUUID(UuidJob.class, UUID.randomUUID());
        return "<html><body>Job queued.</body></html>";
    }
}
