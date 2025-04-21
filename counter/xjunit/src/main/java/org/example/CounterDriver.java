package org.example;
import com.ut.UT_Counter;

import java.util.Random;
import java.util.function.Consumer;


public class CounterDriver {
    private UT_Counter dut = new UT_Counter();
    private int res = 0;

    CounterDriver(){
        dut.InitClock("clk");
        dut.StepRis(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                if (dut.count.U().intValue() == 1){
                    res += 1;
                }
            }
        });
    }

    public int tick(){
        dut.Step();
        return dut.count.U64().intValue();
    }

    public void reset(){
        dut.rst.Set(1);
        dut.Step();
        dut.rst.Set(0);
    }

    public void finish(){
        dut.Finish();
//        System.out.println(res);
    }

    public static void main(String[] args) {
        CounterDriver counter = new CounterDriver();
        counter.reset();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            int chosen = rand.nextInt(100);
            if (chosen < 31) {
                counter.reset();
            }
            System.gc();
        }
    }
}

