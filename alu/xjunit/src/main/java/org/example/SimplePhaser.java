package org.example;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;



import java.util.ArrayList;
import java.util.List;

public class SimplePhaser {
    private int totalParties;
    private int arrived = 0;
    private int phase = 0;
    private int deregistered = 0;
    private final List<Fiber<?>> waiters = new ArrayList<>();
    private final List<Fiber<?>> advanceWaiters = new ArrayList<>();
    private final String name;

    public SimplePhaser(int totalParties, String name) {
        this.totalParties = totalParties;
        this.name = name;
    }

    public int getPhase() {
        return phase;
    }

    public void register() {
        totalParties++;
    }

    public void register(int parties) {
        totalParties += parties;
    }

    @Suspendable
    public void arriveAndAwaitAdvance() throws SuspendExecution {
        arrived++;
        if (arrived >= totalParties - deregistered) {
            release();
        } else {
            waiters.add(Fiber.currentFiber());
//            System.out.println("fiber " + Fiber.currentFiber().getName() + " pending... at phaser " + name + " stage " + phase + " at system time " + System.currentTimeMillis());
            Fiber.park();
        }
    }

    @Suspendable
    public void arriveAndDeregister() {
//        System.out.println("fiber " + Fiber.currentFiber().getName() + " deregister phaser" + name + " at phaser " + phase + " at system time " + System.currentTimeMillis());
        deregistered++;
        if (arrived >= totalParties - deregistered) {
            release();
        }
    }

    @Suspendable
    void release(){
//        System.out.println("fiber " + Fiber.currentFiber().getName() + " release all " +(arrived - 1)+" waiters on phaser " + name + " stage " + phase + " at system time " + System.currentTimeMillis());
        phase++;
        for (Fiber<?> f : waiters) {
            f.unpark();
        }
        waiters.clear();
        for (Fiber<?> f : advanceWaiters) {
            f.unpark();
        }
        advanceWaiters.clear();
        arrived = 0;
    }

    public boolean isTerminated() {
        return deregistered == totalParties;
    }

    @Suspendable
    public int awaitAdvance(int previousPhase, Fiber<?> fiber) throws SuspendExecution {
        if (phase > previousPhase || isTerminated()) {
            return phase;
        }
        if (!advanceWaiters.contains(fiber)) {
            advanceWaiters.add(fiber);
        }
        Fiber.park();
        return phase;
    }
}


