package com.datarangers.profile;

public enum ItemMethod {
    SET("__item_set"),
    UNSET("__item_unset"),
    DELETE("__item_delete");

    private String method;

    ItemMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
