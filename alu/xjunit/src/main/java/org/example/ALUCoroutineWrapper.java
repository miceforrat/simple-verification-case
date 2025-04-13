package org.example;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.ut.UT_ALU;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ALUCoroutineWrapper {
    private UT_ALU alu;
    private final List<SuspendableRunnable> runnables = new ArrayList<>();

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    FiberScheduler scheduler = new FiberExecutorScheduler("single-thread", singleThreadExecutor);

    private ClockManager clockManager;

    public ALUCoroutineWrapper() throws ExecutionException, InterruptedException {
        Fiber<?> creator = new Fiber<>(scheduler, () -> {
            alu = new UT_ALU();
            clockManager = new ClockManager(alu.xclock, 0);
        }).start();
        creator.join();
    }

    @Suspendable
    public void aFunc(int a) throws SuspendExecution  {
        alu.a.Set(a);
        clockManager.step();
    }

    @Suspendable
    public void bFunc(int b)throws SuspendExecution {
        alu.b.Set(b);
        clockManager.step();
    }

    @Suspendable
    public void selFunc(int sel)throws SuspendExecution {
        alu.alu_sel.Set(sel);
        clockManager.step();
    }

    @Suspendable
    public int resFunc() throws SuspendExecution {
        clockManager.step();
        return alu.alu_out.U().intValue();
    }

    public void addRunnable(SuspendableRunnable runnable) {
        runnables.add(runnable);
        clockManager.register();
    }

    public void start() throws SuspendExecution, ExecutionException, InterruptedException {
        clockManager.start(scheduler);
        for (SuspendableRunnable runnable: runnables) {
            new Fiber<>(scheduler, ()->{
                runRunnable(runnable);
            }).start();
        }
        this.clockManager.join();
    }

    private void runRunnable(SuspendableRunnable runnable) throws SuspendExecution, InterruptedException {
        runnable.run();
        clockManager.deregister();
    }
}
