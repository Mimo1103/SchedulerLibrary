package com.mimo.scheduler;

import com.mimo.scheduler.aftertask.AfterTask;

public class TestFunctions {

    @AfterTask(name = "canYouReadMe")
    public static void printHello() {
        System.out.println("Hello");
    }

    public static void addNum(int a, int b) {
        System.out.println("Result: " + (a+b));
    }


    public static void subNum(int a, int b) {
        System.out.println("Result: " + (a-b));
    }

    public static double division(double a, double b) {
        double result = a / b;
        return  result;
    }
}
