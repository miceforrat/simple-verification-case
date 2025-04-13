package org.example;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import com.ut.UT_ALU;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ALUTest {
    
    private int limit = 0xFF;
    private ALUWrapper alu;
    @Before
    public void setUp() throws Exception {
//        alu = new ALUWrapper();
        alu = new ALUWrapper();
        System.out.println("Test setup at " + System.currentTimeMillis());
    }

    @After
    public void tearDown() throws Exception {
        alu.finishDut();
        System.out.println("Test finish");
    }


//    @Test
//    public void testAll(){
//        System.out.println("ALUTestAll setUp at " + System.currentTimeMillis());
//        for (int a = 0; a < 256; a++){
//            for (int b = 0; b < 256; b++){
//                for (int c = 0; c < 16; c++){
//
//                    assertEquals(refModel(a, b, c), alu.process(a, b, c));
//                }
//            }
//        }
//        System.out.println("ALUTestAll finished at " + System.currentTimeMillis());
//    }
//
//    @Test
//    public void testAdd() {
//        System.out.println("ALUTestAdd setUp at " + System.currentTimeMillis());
//
//        for (int a = 0; a < 256; a++){
//            for (int b = 0; b < 256; b++){
//                assertEquals((a+b) & limit, alu.process(a, b, 0));
//            }
//        }
//
//        System.out.println("ALUTestAdd finished at " + System.currentTimeMillis());
//    }
//
//    @Test
//    public void randTest(){
//        Random random = new Random();
//        random.setSeed(System.currentTimeMillis());
//        for (int i = 0 ; i < 100000; i++){
//            int a = random.nextInt(256);
//            int b = random.nextInt( 256);
//            int c = random.nextInt( 16);
//            assertEquals(refModel(a, b, c), alu.process(a, b, c));
//
//        }
//    }

//    @Test
//    public void testCoroutine() throws SuspendExecution, InterruptedException, ExecutionException {
//        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//        FiberScheduler scheduler = new FiberExecutorScheduler("single-thread", singleThreadExecutor);
//        AtomicReference<UT_ALU> alu1 = new AtomicReference<>();
//
//        Fiber<?> creator = new Fiber<>(scheduler, ()->{
//            alu1.set(new UT_ALU());
//        }).start();
//
//        creator.join();
//
//        ClockManager clockManager = new ClockManager(alu1.get().xclock, 2);
//
//
//        List<Fiber<?>> fibers = new ArrayList<>();
//
//        fibers.add(new Fiber<>(scheduler, ()->{
//            Random r = new Random();
//            for (int i = 0; i < 100; i++) {
//                int a = r.nextInt(256);
//                int b = r.nextInt(256);
//                alu1.get().a.Set(a);
//                alu1.get().b.Set(b);
//                clockManager.step();
//                System.err.println(alu1.get().alu_out.U().intValue());
//            }
//            clockManager.deregister();
//        }));
//
//        fibers.add(new Fiber<>(scheduler, ()->{
//            Random r = new Random();
//            for (int i = 0; i < 100; i++) {
//                int sel = r.nextInt(16);
//                alu1.get().alu_sel.Set(sel);
//                clockManager.step();
//            }
//            clockManager.deregister();
//        }));
//
//        clockManager.start(scheduler);
//
//        for (Fiber<?> fiber: fibers) {
//            fiber.start();
//        }
//
//        clockManager.join();
//
//        System.out.println(">> All fibers finished.");
//        singleThreadExecutor.shutdown();
//        singleThreadExecutor.awaitTermination(1, TimeUnit.SECONDS);
//    }

    @Test
    public void testMoreClosed() throws Exception {
        ALUCoroutineWrapper alucoroutine = new ALUCoroutineWrapper();
        DataProducer dataProducer = new DataProducer();

        alucoroutine.addRunnable(dataProducer::produce);

        final int TIMES = 10000000;
        alucoroutine.addRunnable(() ->{
//            Random r = new Random();
            for (int i = 0; i < TIMES; i++) {
//                System.out.println("a:" + dataProducer.a);
                alucoroutine.aFunc(dataProducer.a);
//                dataProducer.produce();
            }
        });

        alucoroutine.addRunnable(() ->{
            Random r = new Random();
            for (int i = 0; i < TIMES; i++) {
//                System.out.println("b:" + dataProducer.b);

                alucoroutine.bFunc(dataProducer.b);
            }
        });

        alucoroutine.addRunnable(() ->{
            Random r = new Random();
            for (int i = 0; i < TIMES; i++) {

                alucoroutine.selFunc(dataProducer.sel);
            }
        });
        alucoroutine.addRunnable(() ->{
            for (int i = 0; i < TIMES; i++) {
                int out = dataProducer.out;
                int res = alucoroutine.resFunc();
                assertEquals( out, res);
            }
        });

        alucoroutine.start();
    }

    private int refModel(int a, int b, int sel){
        switch (sel) {
            case 0b0000: return  (a + b) & limit;
            case 0b0001: return  (a - b) & limit;
            case 0b0010: return  (a * b) & limit;
            case 0b0011: return b != 0 ? a / b : 0;
            case 0b0100: return (a << 1) & limit;
            case 0b0101: return (a >> 1) & limit;
            case 0b0110: return ((a << 1) | (a >> 7)) & limit;
            case 0b0111: return ((a >> 1) | (a << 7)) & limit;
            case 0b1000: return a & b;
            case 0b1001: return a | b;
            case 0b1010: return a ^ b;
            case 0b1011: return ~(a | b) & limit;
            case 0b1100: return ~(a & b) & limit;
            case 0b1101: return ~(a ^ b) & limit;
            case 0b1110: return a > b ? 1 : 0;
            case 0b1111: // Equality
                return a == b ? 1 : 0;
            default:
                return (a + b) & limit;
        }

    }


}
