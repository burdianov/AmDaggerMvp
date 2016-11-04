package com.testography.am_mvp.di;

import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DaggerService {
    public static final String TAG = "DaggerService";
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

    public static void unregisterScope(Class<? extends Annotation>
                                               scopeAnnotation) {
        Iterator<Map.Entry<Class, Object>> iterator = sComponentMap.entrySet()
                .iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, Object> entry = iterator.next();
            if (entry.getKey().isAnnotationPresent(scopeAnnotation)) {
                Log.e(TAG, "unregisterScope: " + entry.getKey().getName());
                iterator.remove();
            }
        }
    }
}
