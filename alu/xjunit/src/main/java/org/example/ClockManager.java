package org.example;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import com.xspcomm.XClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClockManager {
    private final SimplePhaser fiberPhaser;
    private final SimplePhaser mainPhaser;
//    private final FiberScheduler fiberScheduler;
    private final XClock xClock;
    Fiber<?> currentFiber;

    private final List<Runnable> stepDealings = new ArrayList<>();

    private boolean launched = false;


    public void register(){
        register(1);
    }

    public void register(int num){
        fiberPhaser.register(num);
        mainPhaser.register(num);
    }

    public ClockManager(XClock clock) {
        this(clock, 0);
//        currentFiber = new Fiber<>(scheduler, this::runHookFiber).start();
    }

    public ClockManager(XClock clock, int parties) {
        this.fiberPhaser = new SimplePhaser(parties, "fibers");
        this.mainPhaser = new SimplePhaser(parties+1, "main");
//        this.fiberScheduler = scheduler;
        this.xClock = clock;
    }

    @Suspendable
    public void start(FiberScheduler scheduler) throws SuspendExecution {
        launched = true;
        currentFiber = new Fiber<>(scheduler, this::runHookFiber).start();
    }

    @Suspendable
    private void runHookFiber() throws SuspendExecution, InterruptedException {
        int lastPhase = fiberPhaser.getPhase();
        System.out.println("HOOK before:"+lastPhase);
        while (!fiberPhaser.isTerminated()) {
            int current = fiberPhaser.awaitAdvance(lastPhase, Fiber.currentFiber());
//            System.out.println(">>> HOOK: Phase " + lastPhase + " logic here <<< in fiber " + Fiber.currentFiber().getName());
//            System.out.println(Thread.currentThread().getName());
            xClock.Step();
            for (Runnable r : stepDealings) {
                r.run();
            }
//            System.out.println("HOOK after:"+current);
            mainPhaser.arriveAndAwaitAdvance();
            lastPhase = current;
        }
        mainPhaser.arriveAndDeregister();
    }

    @Suspendable
    private void coroutineStep() throws SuspendExecution {
        fiberPhaser.arriveAndAwaitAdvance();
        mainPhaser.arriveAndAwaitAdvance();
    }

    @Suspendable
    public void step() throws SuspendExecution {
        if (!launched) {
            xClock.Step();
        } else {
            coroutineStep();
        }
    }

    @Suspendable
    public void deregister() {
        fiberPhaser.arriveAndDeregister();
        mainPhaser.arriveAndDeregister();
    }

    public void join() throws InterruptedException, ExecutionException {
        this.currentFiber.join();
    }

    public void stepRis(Runnable r) throws InterruptedException, ExecutionException {
        stepDealings.add(r);
    }
}
