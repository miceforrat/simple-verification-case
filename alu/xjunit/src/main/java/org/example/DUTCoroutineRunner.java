package org.example;

import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import com.ut.UT_ALU;
import com.xspcomm.XClock;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DUTCoroutineRunner {
    private final ClockManager clockManager;
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    FiberScheduler scheduler = new FiberExecutorScheduler("single-thread", singleThreadExecutor);

    DUTCoroutineRunner(ClockManager clockManager) {
        this.clockManager = clockManager;
    }

//    private List<Runnable>


}
