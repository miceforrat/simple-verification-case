package org.example;
import com.ut.UT_ALU;

import java.math.BigInteger;
import java.util.function.Consumer;

public class ALUWrapper {
    private static ALUWrapper instance;

    UT_ALU alu;

    ALUWrapper(){
        alu = new UT_ALU();
        System.out.println("new alu wrapper");
//        alu.InitClock("");

//        new Thread(() -> {
//            alu = new UT_ALU();
//        }).run();
//        alu.StepRis(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) {
//
//            }
//        });
    }

    public int process(int a, int b, int sel){
        alu.a.Set(a);
        alu.b.Set(b);
        alu.alu_sel.Set(sel);
        alu.Step();
        return alu.alu_out.U64().intValue();
    }

    public void finishDut(){
        this.alu.Finish();
    }

    public static ALUWrapper getInstance() {
        System.out.println("getInstance");
        if (instance == null) {
            instance = new ALUWrapper();
        }
        System.out.println("getInstance returned");

        return instance;
    }

    public static void finish(){
        if (instance != null) {
            instance.alu.Finish();
        }
    }
}