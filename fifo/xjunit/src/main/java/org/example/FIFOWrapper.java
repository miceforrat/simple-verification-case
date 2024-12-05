package org.example;

import com.ut.UT_FIFO;

public class FIFOWrapper {
    private UT_FIFO fifo = new UT_FIFO();
    FIFOWrapper(){
        fifo.InitClock("clk");
        fifo.rst_n.Set(1);
//        fifo.Step();
    }

    public FIFOWriteRet write(int wdata){
        fifo.wdata.Set(wdata);
        fifo.wr_en.Set(1);
        fifo.Step();
        fifo.wr_en.Set(0);
        return new FIFOWriteRet(fifo.empty.U().intValue(), fifo.full.U().intValue());
    }

    public FIFOReadRet read(){
        fifo.rd_en.Set(1);
        fifo.Step();
        fifo.rd_en.Set(0);
        fifo.Step();
        return new FIFOReadRet(fifo.rdata.U().intValue(), fifo.empty.U().intValue(), fifo.full.U().intValue());
    }


}