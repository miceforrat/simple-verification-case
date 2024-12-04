package org.example;
import com.ut.UT_Counter;

import java.math.BigInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class CounterDriver {
    private UT_Counter dut = new UT_Counter();

    CounterDriver(){
        dut.InitClock("clk");
    }

    public BigInteger tick(){
        dut.Step();
        return dut.count.U64();
    }

    public void reset(){
        dut.rst.Set(1);
        dut.Step();
        dut.rst.Set(0);
    }




}

