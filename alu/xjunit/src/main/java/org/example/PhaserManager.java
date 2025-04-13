package org.example;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class PhaserManager {
    private final SimplePhaser fiberPhaser;
    private final SimplePhaser mainPhaser;
    Fiber<?> currentFiber;


    public void register(){
        fiberPhaser.register();
        mainPhaser.register();
    }

    public PhaserManager(int parties, FiberScheduler scheduler) {
        this.fiberPhaser = new SimplePhaser(parties, "fibers");
        this.mainPhaser = new SimplePhaser(parties + 1, "main");
        currentFiber = new Fiber<>(scheduler, this::runHookFiber).start();
    }

    private void runHookFiber() throws SuspendExecution, InterruptedException {
        int lastPhase = fiberPhaser.getPhase();
        System.out.println("HOOK before:"+lastPhase);
        while (!fiberPhaser.isTerminated()) {
            int current = fiberPhaser.awaitAdvance(lastPhase, Fiber.currentFiber());
            System.out.println(">>> HOOK: Phase " + lastPhase + " logic here <<< in fiber " + Fiber.currentFiber().getName());
            mainPhaser.arriveAndAwaitAdvance();
            lastPhase = current;
        }
        mainPhaser.arriveAndDeregister();
    }

    @Suspendable
    public void arriveAndWait() throws SuspendExecution {
        fiberPhaser.arriveAndAwaitAdvance();
        mainPhaser.arriveAndAwaitAdvance();
    }

    @Suspendable
    public void arriveAndDeregister() {
        fiberPhaser.arriveAndDeregister();
        mainPhaser.arriveAndDeregister();
    }

    public void join() throws InterruptedException, ExecutionException {
        this.currentFiber.join();
    }
}
