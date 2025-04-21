package org.example;

import com.ut.UT_ALU;
import org.xaspect.datas.*;
import org.xaspect.*;

import java.math.BigInteger;

public interface ALUDutDao
        extends DUTDao <UT_ALU> {

    @PostMethod
    void postIn(@Pin("a") int a, @Pin("b") int b, @Pin("alu_sel") int sel);

    @GetMethod(prefix = "alu_out") @Pin
    int getOut();
}
