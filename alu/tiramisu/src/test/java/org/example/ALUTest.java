package org.example;
//import com.ut.UT_ALU;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xaspect.AutoDUTDao;


import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.Random;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.Assert.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
public class ALUTest {
    

//    UT_ALU alu;
//
//    @AutoDUTDao
//    ALUDutDao dao;


    @BeforeEach
    public void setUp() throws Exception {
//        alu = new UT_ALU();
////        System.err.println(alu);
//        dao.bind(alu);
    }

    @AfterEach
    public void tearDown() throws Exception {
//        this.alu.Finish();
    }

//    @Test
//    public void testAll(){
//        Ref ref = new Ref();
//        ALUWrapper wrapper = new ALUWrapper();
////        for (int sel = 0; sel < 16; sel++) {
////            for (int i = 0; i < 20000; i++) {
////                int a = rand.nextInt(256);
////                int b = rand.nextInt(256);
////                assertEquals(ref.refModel(a, b, sel), process(a, b, sel));
////            }
////        }
//        for (int sel = 0; sel < 16; sel++) {
//            for (int a = 0; a < 256; a++){
//                for (int b = 0; b < 256; b++){
//                    assertEquals(ref.refModel(a, b, sel), wrapper.process(a, b, sel));
//                }
//            }
//        }
//        wrapper.alu.Finish();
//    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
    void testAllBySel(int sel) {
        Ref ref = new Ref();
        ALUWrapper wrapper = new ALUWrapper();
//        for (int i = sel; i <= sel + 1; i++) {
            for (int a = 0; a < 256; a++) {
                for (int b = 0; b < 256; b++) {
                    assertEquals(ref.refModel(a, b, sel), wrapper.process(a, b, sel), String.format("Mismatch at a=%d, b=%d, sel=%d", a, b, sel));
                }
            }
        wrapper.alu.Finish();
//        }
    }


//    public int process(int a, int b, int sel) {
//        dao.postIn(a, b, sel);
//        alu.Step();
//        return dao.getOut();
//    }

    private static class Ref {
        private int refModel(int a, int b, int sel) {
            int limit = 0xFF;
            switch (sel) {
                case 0b0000:
                    return (a + b) & limit;
                case 0b0001:
                    return (a - b) & limit;
                case 0b0010:
                    return (a * b) & limit;
                case 0b0011:
                    return b != 0 ? a / b : 0;
                case 0b0100:
                    return (a << 1) & limit;
                case 0b0101:
                    return (a >> 1) & limit;
                case 0b0110:
                    return ((a << 1) | (a >> 7)) & limit;
                case 0b0111:
                    return ((a >> 1) | (a << 7)) & limit;
                case 0b1000:
                    return a & b;
                case 0b1001:
                    return a | b;
                case 0b1010:
                    return a ^ b;
                case 0b1011:
                    return ~(a | b) & limit;
                case 0b1100:
                    return ~(a & b) & limit;
                case 0b1101:
                    return ~(a ^ b) & limit;
                case 0b1110:
                    return a > b ? 1 : 0;
                case 0b1111: // Equality
                    return a == b ? 1 : 0;
                default:
                    return (a + b) & limit;
            }
        }
    }



}
