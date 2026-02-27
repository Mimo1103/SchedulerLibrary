package com.mimo.scheduler;

import com.mimo.scheduler.aftertask.AfterTask;
import com.mimo.scheduler.aftertask.AfterTaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Scheduler scheduler = new Scheduler(2);

        scheduler.addCheckingClass(Main.class);

        scheduler.scheduleWithFixedDelay(1, TimeUnit.SECONDS, () -> printQuestion(), "dialogEvent");

        scheduler.shutdown();

    }

    @AfterTask(name = "dialogEvent")
    public static void printHello() {
        System.out.println("Hello!");
    }

    @AfterTask(name = "contentEvent")
    public static void printQuestion() {
        System.out.println("Question");
    }

    public static void printAnswer() {
        System.out.println("Answer");
    }

    @AfterTask(name = "dialogEvent")
    public static void printBye() {
        System.out.println("Bye!");
    }
}

