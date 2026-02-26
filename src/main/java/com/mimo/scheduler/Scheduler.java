package com.mimo.scheduler;

import com.mimo.scheduler.aftertask.AfterTaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Scheduler {
    private final ScheduledExecutorService executor;
    private final AfterTaskExecutor afterTaskExecutor;


    Scheduler(int numThreads) {
        executor =  Executors.newScheduledThreadPool(2);
        afterTaskExecutor = new AfterTaskExecutor();
    }



    /**
     * Schedules a {@link Runnable} to execute immediately.
     *
     * @param task the {@code Runnable} to execute
     * */
    public CompletableFuture<?> run(Runnable task) {
        return (CompletableFuture<?>) executor.submit(task);
    }

    /**
     * Schedules a {@link Runnable} to execute immediately.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param task the {@code Runnable} to execute
     * @param eventName the name of the event fired after completion
     * */
    public CompletableFuture<?> run(Runnable task, String eventName) throws InvocationTargetException, IllegalAccessException {
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
        return (CompletableFuture<?>) executor.submit(task);
    }

    /**
     * Schedules a {@link Callable} to execute immediately and returns a {@link CompletableFuture}.
     *
     * @param task the {@code Callable<?>} to execute
     *
     * @return a {@link CompletableFuture} that completes when the task finishes
     **/
    public CompletableFuture<?> run(Callable<?> task) {
        return (CompletableFuture<?>) executor.submit(task);
    }

    /**
     * Schedules a {@link Callable} to execute immediately and returns a {@link CompletableFuture}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param task the {@code Callable<?>} to execute
     * @param eventName the name of the event fired after completion
     *
     * @return a {@link CompletableFuture} that completes when the task finishes
     * */
    public CompletableFuture<?> run(Callable<?> task, String eventName) throws InvocationTargetException, IllegalAccessException {
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
        return (CompletableFuture<?>) executor.submit(task);
    }


    /**
     * Schedules a {@link Runnable} to execute after the {@code delay} in the given {@link TimeUnit}.
     *
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute
     * */
    public ScheduledFuture<?> scheduleWithFixedDelay(long delay, TimeUnit unit, Runnable task) {
        return executor.schedule(task, delay, unit);
    }

    /**
     * Schedules a {@link Runnable} to execute after the {@code delay} in the given {@link TimeUnit}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute after a delay
     * @param eventName the name of the event fired after completion
     * */
    public ScheduledFuture<?> scheduleWithFixedDelay(long delay, TimeUnit unit, Runnable task, String eventName) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        ScheduledFuture<?> scheduledFuture = executor.schedule(task, delay, unit);
        Thread.sleep(getIntermission(scheduledFuture));
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
        return scheduledFuture;
    }

    /**
     * Schedules a {@link Callable} to execute after the {@code delay} in the given {@link TimeUnit} and returns a {@link ScheduledFuture}.
     *
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Callable<T>} to execute after a delay
     *
     * @return a {@link ScheduledFuture} that completes when the task finishes
     * */
    public <T> ScheduledFuture<T> scheduleWithFixedDelay(long delay, TimeUnit unit, Callable<T> task) {
        return executor.schedule(task, delay, unit);
    }

    /**
     * Schedules a {@link Callable} to execute after the {@code delay} in the given {@link TimeUnit} and returns a {@link ScheduledFuture}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Callable<T>} to execute after a delay
     * @param eventName the name of the event fired after completion
     *
     * @return a {@link ScheduledFuture} that completes when the task finishes
     * */
    public <T> ScheduledFuture<T> scheduleWithFixedDelay(long delay, TimeUnit unit, Callable<T> task, String eventName) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        ScheduledFuture<T> scheduledFuture = executor.schedule(task, delay, unit);
        Thread.sleep(getIntermission(scheduledFuture));
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
        return scheduledFuture;
    }



    /**
     * Schedules a {@link Runnable} to execute if the given {@link LocalDateTime} was before now.
     *
     * @param localDateTime the {@code LocalDateTime} at which the {@code Runnable} runs
     * @param task the {@code Runnable} to execute at a specific LocalDateTime
     * */
    public CompletableFuture<?> scheduleAtFixedLocalDateTime(LocalDateTime localDateTime, Runnable task) {
        if(localDateTime.isAfter(LocalDateTime.now())) {
            while(localDateTime.isBefore(LocalDateTime.now())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return (CompletableFuture<?>) executor.submit(task);
        }
        return null;
    }

    /**
     * Schedules a {@link Runnable} to execute if the given {@link LocalDateTime} was before now.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param localDateTime the {@code LocalDateTime} at which the {@code Runnable} runs
     * @param task the {@code Runnable} to execute at a specific LocalDateTime
     * @param eventName the name of the event fired after completion
     * */
    public CompletableFuture<?> scheduleAtFixedLocalDateTime(LocalDateTime localDateTime, Runnable task, String eventName) throws InvocationTargetException, IllegalAccessException {
        if(localDateTime.isAfter(LocalDateTime.now())) {
            while(localDateTime.isBefore(LocalDateTime.now())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
            return (CompletableFuture<?>) executor.submit(task);
        }
        return null;
    }



    /**
     * Schedules a {@link Callable} to execute if the given {@link LocalDateTime} was before now and returns a {@link CompletableFuture}.
     *
     * @param localDateTime the {@code LocalDateTime} at which the {@code Callable<T>} runs
     * @param task the {@code CompletableFuture<T>} to execute at a specific LocalDateTime
     *
     * @return a {@link CompletableFuture} that completes when the task finishes
     * */
    public <T> CompletableFuture<T> scheduleAtFixedLocalDateTime(LocalDateTime localDateTime, Callable<T> task) {
        if(localDateTime.isAfter(LocalDateTime.now())) {
            while(localDateTime.isBefore(LocalDateTime.now())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return (CompletableFuture<T>) executor.submit(task);
        }
        return null;
    }

    /**
     * Schedules a {@link Callable} to execute if the given {@link LocalDateTime} was before now and returns a {@link CompletableFuture}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     *
     * @param localDateTime the {@code LocalDateTime} at which the {@code Callable<T>} runs
     * @param task the {@code CompletableFuture<T>} to execute at a specific LocalDateTime
     * @param eventName the name of the event fired after completion
     *
     * @return a {@link CompletableFuture} that completes when the task finishes
     * */
    public <T> CompletableFuture<T> scheduleAtFixedLocalDateTime(LocalDateTime localDateTime, Callable<T> task, String eventName) throws InvocationTargetException, IllegalAccessException {
        if(localDateTime.isAfter(LocalDateTime.now())) {
            while(localDateTime.isBefore(LocalDateTime.now())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
            return (CompletableFuture<T>) executor.submit(task);
        }
        return null;
    }


    /**
     * Schedules a {@link Runnable} to execute {@code numRepeats} amount of times with each having their own {@code delay} using the given {@code unit}.
     * The first {@link Runnable} has no delay and gets executed immediately.
     *
     * @param numRepeats the number of repeats of the {@code Runnable}
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute repeatedly
     * */
    public void scheduleAtFixedRateNow(int numRepeats, long delay, TimeUnit unit, Runnable task) throws InterruptedException {
        for (int i = 0; i < numRepeats; i++) {
            ScheduledFuture<?> scheduledFuture = scheduleWithFixedDelay(delay, unit, task);
            Thread.sleep(getIntermission(scheduledFuture));
        }
    }

    /**
     * Schedules a {@link Runnable} to execute {@code numRepeats} amount of times with each having their own {@code delay} using the given {@code unit}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}
     * The first {@link Runnable} has no delay and gets executed immediately.
     *
     * @param numRepeats the number of repeats of the {@code Runnable}
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute repeatedly
     * @param eventName the name of the event fired after completion
     * */
    public void scheduleAtFixedRateNow(int numRepeats, long delay, TimeUnit unit, Runnable task, String eventName) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < numRepeats; i++) {
            ScheduledFuture<?> scheduledFuture = scheduleWithFixedDelay(delay, unit, task);
            Thread.sleep(getIntermission(scheduledFuture));
        }
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }



    /**
     * Schedules a {@link Runnable} to execute {@code numRepeats} amount of times with each having their own {@code delay} using the given {@code unit}.
     * The first {@link Runnable} has no delay and gets executed immediately.
     *
     * @param numRepeats the number of repeats of the {@code Runnable}
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute repeatedly
     * */
    public void scheduleAtFixedRate(int numRepeats, long delay, TimeUnit unit, Runnable task) throws InterruptedException {
        for (int i = 0; i < numRepeats; i++) {
            ScheduledFuture<?> scheduledFuture = scheduleWithFixedDelay(delay, unit, task);
            Thread.sleep(getIntermission(scheduledFuture));
        }
    }

    /**
     * Schedules a {@link Runnable} to execute {@code numRepeats} amount of times with each having their own {@code delay} using the given {@code unit}.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}.
     * The first {@link Runnable} has no delay and gets executed immediately.
     *
     * @param numRepeats the number of repeats of the {@code Runnable}
     * @param delay the delay after which the task runs
     * @param unit the {@code TimeUnit} to describe the delay length
     * @param task the {@code Runnable} to execute repeatedly
     * @param eventName the name of the event fired after completion
     * */
    public void scheduleAtFixedRate(int numRepeats, long delay, TimeUnit unit, Runnable task, String eventName) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < numRepeats; i++) {
            ScheduledFuture<?> scheduledFuture = scheduleWithFixedDelay(delay, unit, task);
            Thread.sleep(getIntermission(scheduledFuture));
        }
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }


    /**
     * Deletes a {@link ScheduledFuture}. When the {@link ScheduledFuture} is already running it will also try to stop it from running.
     *
     * @param scheduledFuture the {@code scheduledFuture} to delete
     * */
    public void delete(ScheduledFuture<?> scheduledFuture) {
        scheduledFuture.cancel(true);
    }

    /**
     * Deletes a {@link ScheduledFuture}. When the {@link ScheduledFuture} is already running it will also try to stop it from running.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}.
     *
     * @param scheduledFuture the {@code scheduledFuture} to delete
     * @param eventName the name of the event fired after completion
     * */
    public void delete(ScheduledFuture<?> scheduledFuture, String eventName) throws InvocationTargetException, IllegalAccessException {
        scheduledFuture.cancel(true);
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }

    /**
     * Deletes a {@link CompletableFuture}. When the {@link CompletableFuture} is already running it will also try to stop it from running.
     *
     * @param completableFuture the {@code completableFuture} to delete
     * */
    public void delete(CompletableFuture<?> completableFuture) {
        completableFuture.cancel(true);
    }

    /**
     * Deletes a {@link CompletableFuture}. When the {@link CompletableFuture} is already running it will also try to stop it from running.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}.
     *
     * @param completableFuture the {@code completableFuture} to delete
     * @param eventName the name of the event fired after completion
     * */
    public void delete(CompletableFuture<?> completableFuture, String eventName) throws InvocationTargetException, IllegalAccessException {
        completableFuture.cancel(true);
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }


    /**
     * Deletes all {@link ScheduledFuture} instances. When the {@link ScheduledFuture} instances are already running it will also try to stop them from running.
     *
     * @param scheduledFutures the {@code ScheduledFuture} instances to delete
     * */
    public void deleteAll(ScheduledFuture<?>... scheduledFutures) {
        for (ScheduledFuture<?> scheduledFuture : scheduledFutures) {
            delete(scheduledFuture);
        }
    }

    /**
     * Deletes all {@link ScheduledFuture} instances. When the {@link ScheduledFuture} instances are already running it will also try to stop them from running.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}.
     *
     * @param eventName the name of the event fired after completion
     * @param scheduledFutures the {@code ScheduledFuture} instances to delete
     * */
    public void deleteAll(String eventName, ScheduledFuture<?>... scheduledFutures) throws InvocationTargetException, IllegalAccessException {
        for (ScheduledFuture<?> scheduledFuture : scheduledFutures) {
            delete(scheduledFuture);
        }
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }


    /**
     * Deletes all {@link CompletableFuture} instances. When the {@link CompletableFuture} instances are already running it will also try to stop them from running.
     *
     * @param completableFutures the {@code CompletableFuture} instances to delete
     * */
    public void deleteAll(CompletableFuture<?>... completableFutures) {
        for (CompletableFuture<?> completableFuture : completableFutures) {
            delete(completableFuture);
        }
    }

    /**
     * Deletes all {@link CompletableFuture} instances. When the {@link CompletableFuture} instances are already running it will also try to stop them from running.
     * Additionally, this also invokes every static method with the {@link com.mimo.scheduler.aftertask.AfterTask} annotation and the given {@code eventName}.
     *
     * @param eventName the name of the event fired after completion
     * @param completableFutures the {@code CompletableFuture<?>} instances to delete
     * */
    public void deleteAll(String eventName, CompletableFuture<?>... completableFutures) throws InvocationTargetException, IllegalAccessException {
        for (CompletableFuture<?> completableFuture : completableFutures) {
            delete(completableFuture);
        }
        afterTaskExecutor.invokeMethods(afterTaskExecutor.getSpecificMethods(eventName));
    }


    /**
     * Returns the remaining delay of the {@link  ScheduledFuture} in milliseconds with additional 3 milliseconds for safety.
     *
     * @param scheduledFuture the {@code scheduledFuture<?>} to get delay from
     *
     * @return a long representing the remaining delay of the {@link ScheduledFuture} in milliseconds
     * */
    public long getIntermission(ScheduledFuture<?> scheduledFuture) {
        return scheduledFuture.getDelay(TimeUnit.MILLISECONDS) + 3;
    }


    /**
     * Shuts down the executor immediately and returns a {@code 1} if it sucessfully shut down immediately and returns a {@code 0} if not.
     *
     * @return {@code 1} if shutdown was immediately, {@code 0} otherwise
     * */
    public int shutdownNow() {
        executor.shutdownNow();
        if (executor.isShutdown()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Shuts down the executor and returns a {@code 1} if it successfully shut down immediately and returns a {@code 0} if not.
     *
     * @return {@code 1} if shutdown was immediately, {@code 0} otherwise
     * */
    public int shutdown() {
        executor.shutdown();
        if (executor.isShutdown()) {
            return 1;
        } else {
            return 0;
        }
    }


}
