package org.example;

import com.ut.UT_FIFO;
import org.xaspect.DUTDao;
import org.xaspect.GetMethod;
import org.xaspect.PostMethod;
import org.xaspect.datas.OutBundle;
import org.xaspect.datas.Pin;



public interface FIFODao extends DUTDao<UT_FIFO> {

    @PostMethod
    void reset(@Pin("rst_n") int value);

    @PostMethod
    void setWData(@Pin("wdata") int value);

    @PostMethod
    void setWrEn(@Pin("wr_en")int value);

    @PostMethod
    void setRdEn(@Pin("rd_en")int value);

    @GetMethod
    @OutBundle FIFOReadRet readDataAndStatus();

    @GetMethod
    @OutBundle FIFOWriteRet getAfterWriteStatus();

}
