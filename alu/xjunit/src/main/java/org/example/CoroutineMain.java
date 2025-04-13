package org.example;

import co.paralleluniverse.fibers.*;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.*;

//public class CoroutineMain {
//    static class PhaserManager {
//        private final Phaser fiberPhaser;
//        private final Phaser mainPhaser;
//
//        public void register(){
//            fiberPhaser.register();
//            mainPhaser.register();
//        }
//
//        public PhaserManager(int parties) {
//            this.fiberPhaser = new Phaser(parties);
//            this.mainPhaser = new Phaser(parties+1);
//
//            new Thread(() ->{
//                int lastPhase = fiberPhaser.getPhase();
//                while(!fiberPhaser.isTerminated()) {
//                    int current = fiberPhaser.awaitAdvance(lastPhase);
//                    System.out.println(">>> HOOK: Phase " + current + " logic here <<<");
//                    mainPhaser.arriveAndAwaitAdvance();
//                    lastPhase = current;
//                }
//                this.mainPhaser.arriveAndDeregister();
//            }).start();
//        }
//
//        public void arriveAndWait() {
//            fiberPhaser.arriveAndAwaitAdvance();
//            mainPhaser.arriveAndAwaitAdvance();
//        }
//
//        public void arriveAndDeregister() {
//            fiberPhaser.arriveAndDeregister();
//            mainPhaser.arriveAndDeregister();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        System.out.println("VM args: " + ManagementFactory.getRuntimeMXBean().getInputArguments());
//
//        List<Fiber<?>> fibers = new ArrayList<>();
//
//        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//        FiberScheduler scheduler = new FiberExecutorScheduler("single-thread", singleThreadExecutor);
//
//        int LEN = 3;
//
//        PhaserManager phaserManager = new PhaserManager(LEN);
//
//        // 启动多个 Fiber（比如 2 个）
//        for (int i = 0; i < LEN; i++){
//            final int id = i;
//            Fiber<Object> fiber = new Fiber<>(scheduler, ()->{
//                int randomWait = ThreadLocalRandom.current().nextInt(3, 6);
//                for (int j = 0; j < randomWait; j++){
//                    System.out.println(Thread.currentThread().getName() + "with fiber: " + id + " in phase: " + phaserManager.fiberPhaser.getPhase());
//                    phaserManager.arriveAndWait();
//                }
//
//                System.out.println(Thread.currentThread().getName() + " with fiber: " + id);
//                phaserManager.arriveAndDeregister();
//            }).start();
//            fibers.add(fiber);
//            System.out.println("creating " + id);
//        }
//
//        for (int i = 1; i <= 2; i++) {
//            final int id = i;
//            Fiber<Object> fiber = new Fiber<>(scheduler, () -> {
//                System.out.println(Thread.currentThread().getName() + ": " + id);
//                System.out.println("Fiber " + id + " - Step 1: job1");
//                Fiber.park();
//                System.out.println(Thread.currentThread().getName() + ": " + id);
//                System.out.println("Fiber " + id + " - Step 2: job2");
//                Fiber.park();
//                System.out.println(Thread.currentThread().getName() + ": " + id);
//                System.out.println("Fiber " + id + " - Step 3: job3");
//            }).start();
//            fibers.add(fiber);
//        }
////
////        // 模拟统一调度器：每轮统一推进一段
////        for (int round = 1; round <= 2; round++) {
////            Thread.sleep(1000);
////            System.out.println(">> Main thread resuming all fibers to step " + (round + 1));
////            for (Fiber<?> fiber : fibers) {
////                fiber.unpark();
////            }
////        }
//
//        // 等待所有 Fiber 结束
//        for (Fiber<?> fiber : fibers) {
//            fiber.join();
//        }
//
//        System.out.println(">> All fibers finished.");
//        singleThreadExecutor.shutdown();
//        singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS);
//    }
//}

public class CoroutineMain {

    public static void main(String[] args) throws Exception {
//        System.out.println("VM args: " + ManagementFactory.getRuntimeMXBean().getInputArguments());

        List<Fiber<?>> fibers = new ArrayList<>();

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        FiberScheduler scheduler = new FiberExecutorScheduler("single-thread", singleThreadExecutor);

        int LEN = 4;

        PhaserManager phaserManager = new PhaserManager(LEN, scheduler);

        for (int i = 0; i < LEN; i++) {
            Fiber<Object> fiber = new Fiber<>(scheduler, () -> {
                int randomWait = ThreadLocalRandom.current().nextInt(3, 8);
                for (int j = 0; j < randomWait; j++) {
                    phaserManager.arriveAndWait();
                }
                phaserManager.arriveAndDeregister();
            }).start();
            fibers.add(fiber);
            System.out.println("creating " + i);
        }

        for (Fiber<?> fiber : fibers) {
            fiber.join();
        }

        phaserManager.join();

        System.out.println(">> All fibers finished.");
        singleThreadExecutor.shutdown();
        singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS);
    }
}
