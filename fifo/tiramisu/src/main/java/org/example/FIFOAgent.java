package org.example;

import com.ut.UT_FIFO;
import org.xaspect.AutoDUTDao;

public class FIFOAgent {
    private UT_FIFO fifo = new UT_FIFO();

//    @AutoDUT(clockName = "clk", resetName = "rst_n", covFileName = "test.dat")
//    private FIFODao fifo;

    @AutoDUTDao
    private FIFODao fifoDao;

    FIFOAgent(){
        fifo.InitClock("clk");
        fifoDao.bind(fifo);
        reset();
//        fifo.Step();
    }
    
    public void finish(){
        fifo.Finish();
    }

    public void reset(){
        this.fifoDao.reset(0);
        fifo.Step();
        this.fifoDao.reset(1);
        fifo.Step();
    }

    public FIFOWriteRet write(int wdata){
        fifoDao.setWData(wdata);
        fifoDao.setWrEn(1);
        fifo.Step();
        fifoDao.setWrEn(0);
        return fifoDao.getAfterWriteStatus();
    }

    public FIFOReadRet read(){
        fifoDao.setRdEn(1);
        fifo.Step();
        fifoDao.setRdEn(0);
        fifo.Step();
        return fifoDao.readDataAndStatus();
    }
}