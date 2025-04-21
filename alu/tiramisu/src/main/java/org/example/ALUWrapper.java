package org.example;

import com.ut.UT_ALU;
import org.xaspect.AutoDUTDao;

public class ALUWrapper {

    @AutoDUTDao
    ALUDutDao dao;

    UT_ALU alu;

    ALUWrapper() {
        alu = new UT_ALU();
        dao.bind(alu);
    }

    public int process(int a, int b, int sel) {
        dao.postIn(a, b, sel);
        alu.Step();
        return dao.getOut();
    }

}
