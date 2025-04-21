package org.example;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class ALUTest {
    
    private int limit = 0xFF;
    private ALUWrapper alu;
    @Before
    public void setUp() throws Exception {
//        alu = new ALUWrapper();
        alu = new ALUWrapper();

        System.out.println("Test setup at " + System.currentTimeMillis());
    }

    @After
    public void tearDown() throws Exception {
        alu.finishDut();
        System.out.println("Test finish");
    }


    @Test
    public void testAll(){
        System.out.println("ALUTestAll setUp at " + System.currentTimeMillis());
        for (int a = 0; a < 256; a++){
            for (int b = 0; b < 256; b++){
                for (int c = 0; c < 16; c++){

                    assertEquals(refModel(a, b, c), alu.process(a, b, c));
                }
            }
        }
        System.out.println("ALUTestAll finished at " + System.currentTimeMillis());
    }

    @Test
    public void testAdd() {
        System.out.println("ALUTestAdd setUp at " + System.currentTimeMillis());

        for (int a = 0; a < 256; a++){
            for (int b = 0; b < 256; b++){
                assertEquals((a+b) & limit, alu.process(a, b, 0));
            }
        }

        System.out.println("ALUTestAdd finished at " + System.currentTimeMillis());
    }

    @Test
    public void randTest(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0 ; i < 100000; i++){
            int a = random.nextInt(256);
            int b = random.nextInt( 256);
            int c = random.nextInt( 16);
            assertEquals(refModel(a, b, c), alu.process(a, b, c));

        }
    }

    private int refModel(int a, int b, int sel){
        switch (sel) {
            case 0b0000: return  (a + b) & limit;
            case 0b0001: return  (a - b) & limit;
            case 0b0010: return  (a * b) & limit;
            case 0b0011: return b != 0 ? a / b : 0;
            case 0b0100: return (a << 1) & limit;
            case 0b0101: return (a >> 1) & limit;
            case 0b0110: return ((a << 1) | (a >> 7)) & limit;
            case 0b0111: return ((a >> 1) | (a << 7)) & limit;
            case 0b1000: return a & b;
            case 0b1001: return a | b;
            case 0b1010: return a ^ b;
            case 0b1011: return ~(a | b) & limit;
            case 0b1100: return ~(a & b) & limit;
            case 0b1101: return ~(a ^ b) & limit;
            case 0b1110: return a > b ? 1 : 0;
            case 0b1111: // Equality
                return a == b ? 1 : 0;
            default:
                return (a + b) & limit;
        }
//        if sel == 0b0000:  # Addition
//                result = (a + b) & 0xFF
//        elif sel == 0b0001:  # Subtraction
//                result = (a - b) & 0xFF
//        elif sel == 0b0010:  # Multiplication
//                result = (a * b) & 0xFF
//        elif sel == 0b0011:  # Division
//                result = a // b if b != 0 else 0
//        elif sel == 0b0100:  # Logical left shift
//                result = (a << 1) & 0xFF
//        elif sel == 0b0101:  # Logical right shift
//                result = (a >> 1) & 0xFF
//        elif sel == 0b0110:  # Rotate left
//        result = ((a << 1) | (a >> 7)) & 0xFF
//        elif sel == 0b0111:  # Rotate right
//        result = ((a >> 1) | (a << 7)) & 0xFF
//        elif sel == 0b1000:  # AND
//                result = a & b
//        elif sel == 0b1001:  # OR
//                result = a | b
//        elif sel == 0b1010:  # XOR
//                result = a ^ b
//        elif sel == 0b1011:  # NOR
//                result = ~(a | b) & 0xFF
//        elif sel == 0b1100:  # NAND
//                result = ~(a & b) & 0xFF
//        elif sel == 0b1101:  # XNOR
//                result = ~(a ^ b) & 0xFF
//        elif sel == 0b1110:  # Greater than comparison
//                result = 1 if a > b else 0
//        elif sel == 0b1111:  # Equality comparison
//        result = 1 if a == b else 0
//        else:  # Default case
//                result = (a + b) & 0xFF
    }


}
