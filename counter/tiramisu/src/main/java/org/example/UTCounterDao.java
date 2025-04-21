package org.example;

import com.ut.UT_Counter;
import org.xaspect.*;
import org.xaspect.datas.Pin;

public interface UTCounterDao extends DUTDao<UT_Counter> {

    @PostMethod
    void setRst(@Pin int rst);

    @GetMethod
    @Pin("count")
    int getCnt();
}
