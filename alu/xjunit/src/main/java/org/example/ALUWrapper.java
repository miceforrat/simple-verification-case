package org.example;
import com.ut.UT_ALU;

import java.math.BigInteger;

public class ALUWrapper {
    UT_ALU alu = new UT_ALU();
    ALUWrapper(){
        alu.InitClock("clk");
//        alu.a.AsImmWrite();
//        alu.b.AsImmWrite();
//        alu.alu_sel.AsImmWrite();
    }

    public BigInteger process(int a, int b, int sel){
        alu.a.Set(a);
        alu.b.Set(b);
        alu.alu_sel.Set(sel);
        alu.Step();
        return alu.alu_out.U64();
    }
}