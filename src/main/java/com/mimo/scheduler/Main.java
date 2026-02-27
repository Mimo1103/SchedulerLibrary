package com.mimo.scheduler;

import com.mimo.scheduler.aftertask.AfterTask;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * This class works as a little guide into the SchedulerLibrary and can be replaced by any other class.
 */
public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {

        /// There needs to be a new Scheduler class declared to gain access to the library methods.
        /// The numThreads argument tell the scheduler how many tasks it can do at once. So numThreads = 2
        /// limit the amount of tasks it can handle simultaneously.
        Scheduler scheduler = new Scheduler(2);

        /// To invoke a method just do: [Scheduler].[java.lang.reflect.Method];
        /// At any point you can add, remove, set and more classes to the classes list inside the declared scheduler.
        /// so it's later possible to chain methods together. When trying to add multiple classes at the same time just
        /// separate them with a comma.
        scheduler.addCheckingClass(Main.class);

        /// Here one of the few methods are called: scheduleWithFixedDelay is invoked with some arguments.
        /// The delay and the TimeUnit describe how long it takes for the task to begin running.
        /// The lambda expression functions as a wrapper for Runnables, Callables, ... and tells the given method
        /// what method it should execute after the delay.
        /// The eventName is just an event that gets fired and every other method with the @AfterTask annotation
        /// that has the name argument that matches the given eventName also gets executed. As long as the methods class
        /// is inside the classes list of the Scheduler instance which is why we added a class to the list with:
        /// scheduler.addCheckingClass(Main.class);
        scheduler.scheduleWithFixedDelay(1, TimeUnit.SECONDS, () -> printQuestion(), "dialogEvent");

        /// The previously declared Scheduler instance always needs to be shut down somewhere so the code can
        /// stop and won't run infinitely after.
        scheduler.shutdown();
    }

    /// This method got the @AfterTask annotation with the value for name "dialogEvent".
    /// Because the value for name is the same as the eventName from the called method the
    /// printHello() method is going to execute after the printQuestion() method is finished running.
    /// For a method to be able to be called like this the method needs to be static and have
    /// no parameters it can get elsewhere.
    @AfterTask(name = "dialogEvent")
    public static void printHello() {
        System.out.println("Hello!");
    }

    /// Even though the printQuestion() gets executed it does not execute other methods with the contentEvent
    /// value of the @AfterTask annotation.
    /// Because the name value in the annotation and the given eventName at the top aren't the same this method
    /// isn't executed from the event.
    @AfterTask(name = "contentEvent")
    public static void printQuestion() {
        System.out.println("Question");
    }

    /// This method has no @AfterTask annotation which doesn't allow it to be invoked from any Scheduler.
    public static void printAnswer() {
        System.out.println("Answer");
    }

    /// Here the method has the same name value as the eventName from the top which means it also gets invoked.
    @AfterTask(name = "dialogEvent")
    public static void printBye() {
        System.out.println("Bye!");
    }
}

