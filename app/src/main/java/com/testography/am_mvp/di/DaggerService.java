package com.testography.am_mvp.di;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DaggerService {
    private static Map<Class, Object> sComponentMap = new HashMap<>();

    public static void registerComponent(Class componentClass, Object
            dagggerComponent) {
        sComponentMap.put(componentClass, dagggerComponent);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> componentClass) {
        Object component = sComponentMap.get(componentClass);

        return (T) component;
    }
}
