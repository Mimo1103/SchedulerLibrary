package com.mimo.scheduler;

import com.mimo.scheduler.aftertask.AfterTask;
import com.mimo.scheduler.aftertask.AfterTaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Main {

    @AfterTask(name = "canYouReadMe")
    public static void printBye() {
        System.out.println("Bye!");
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Scheduler scheduler = new Scheduler(2);

        AfterTaskExecutor afterTaskExecutor = new AfterTaskExecutor();

        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods("canYouReadMe", TestFunctions.class, Main.class));

        scheduler.shutdown();




    }
}

