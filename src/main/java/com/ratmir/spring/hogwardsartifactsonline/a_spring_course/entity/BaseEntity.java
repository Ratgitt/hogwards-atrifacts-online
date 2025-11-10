package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {

    T getId();

    void setId(T id);
}
