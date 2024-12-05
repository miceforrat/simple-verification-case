package org.example;
import com.ut.UT_Counter;


public class CounterDriver {
    private UT_Counter dut = new UT_Counter();

    CounterDriver(){
        dut.InitClock("clk");
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
}

