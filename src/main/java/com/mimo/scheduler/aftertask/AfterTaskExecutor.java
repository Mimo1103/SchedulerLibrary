package com.mimo.scheduler.aftertask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class AfterTaskExecutor {

    public ArrayList<Method> getSpecificMethods(String eventName, Class<?>... classes) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Class<?> clazz : classes) {
            for(Method method : clazz.getDeclaredMethods()){
                if(method.isAnnotationPresent(AfterTask.class)){
                    if (method.getAnnotation(AfterTask.class).name().equals(eventName)) {
                        methods.add(method);
                    }
                }
            }
        }
        return methods;
    }

    public ArrayList<Method> getAllMethods(Class<?>... classes) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Class<?> clazz : classes) {
            for(Method method : clazz.getDeclaredMethods()){
                if(method.isAnnotationPresent(AfterTask.class)){
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    public void invokeMethods(ArrayList<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null);
            }
        }
    }
}

