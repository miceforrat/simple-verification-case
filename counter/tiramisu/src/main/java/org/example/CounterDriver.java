package org.example;
import com.ut.UT_Counter;
import org.xaspect.AutoDUTDao;


public class CounterDriver {
    private final UT_Counter dut = new UT_Counter();

    @AutoDUTDao()
    private UTCounterDao counterDao;

    CounterDriver(){
        dut.InitClock("clk");
        counterDao.bind(dut);
        reset();
    }

    public void finish(){
        dut.Finish();
    }

    public int tick(){
        dut.Step();
        return counterDao.getCnt();
//        dut.Step();
//        return dut.count.U64().intValue();
    }

    public void reset(){
        counterDao.setRst(1);
        dut.Step();
        counterDao.setRst(0);
//        dut.rst.Set(1);
//        dut.Step();
//        dut.rst.Set(0);
    }
}

