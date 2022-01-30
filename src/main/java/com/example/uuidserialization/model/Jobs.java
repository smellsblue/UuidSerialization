package com.example.uuidserialization.model;

import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Component
public class Jobs {
    public <PARAM extends Serializable, JOB extends Job<PARAM>> void queue(Class<JOB> jobClass, PARAM parameter) {
        BackgroundJob.<Jobs>enqueue((service) -> service.process(jobClass, parameter));
    }

    public <JOB extends Job<UUID>> void queueUUID(Class<JOB> jobClass, UUID parameter) {
        BackgroundJob.<Jobs>enqueue((service) -> service.processUUID(jobClass, parameter));
    }

    public <PARAM extends Serializable, JOB extends Job<PARAM>> void process(Class<JOB> jobClass, PARAM parameter) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        jobClass.getDeclaredConstructor().newInstance().process(parameter);
    }

    public <JOB extends Job<UUID>> void processUUID(Class<JOB> jobClass, UUID parameter) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        jobClass.getDeclaredConstructor().newInstance().process(parameter);
    }
}
