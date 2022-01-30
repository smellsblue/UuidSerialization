package com.example.uuidserialization.model;

import java.io.Serializable;

public interface Job<T extends Serializable> {
    void process(T param);
}
