package com.ark.studentmonitoring.Model;

import java.util.Objects;

public class ModelMyClass {

    private String key;
    private String myClass;

    public ModelMyClass() {

    }

    public ModelMyClass(String key, String myClass) {
        this.key = key;
        this.myClass = myClass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMyClass() {
        return myClass;
    }

    public void setMyClass(String myClass) {
        this.myClass = myClass;
    }

    @Override
    public String toString() {
        return myClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelMyClass that = (ModelMyClass) o;
        return key.equals(that.key);
    }


}
