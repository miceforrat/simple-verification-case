package org.example;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class ALUTest {
    
    private int limit = 0xFF;
    private ALUWrapper alu;

    @Before
    public void setUp() throws Exception {
        this.alu = new ALUWrapper();
    }

    @Test
    public void testAdd() {

        for (int a = 0; a < 256; a++){
            for (int b = 0; b < 256; b++){
                assertEquals((a+b) & limit, alu.process(a, b, 0));
            }
        }

    }

    @Test
    public void testAll(){

        for (int a = 0; a < 256; a++){
            for (int b = 0; b < 256; b++){
                for (int c = 0; c < 16; c++){
                    assertEquals(refModel(a, b, c), alu.process(a, b, c));
                }
            }
        }
    }

    @Test
    public void randTest(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0 ; i < 100000; i++){
            int a = random.nextInt(0, 256);
            int b = random.nextInt(0, 256);
            int c = random.nextInt(0, 16);
            assertEquals(refModel(a, b, c), alu.process(a, b, c));
        }
    }




    private int refModel(int a, int b, int sel){
        return switch (sel) {
            case 0b0000 -> (a + b) & limit;
            case 0b0001 -> (a - b) & limit;
            case 0b0010 -> (a * b) & limit;
            case 0b0011 -> b != 0 ? a / b : 0;
            case 0b0100 -> (a << 1) & limit;
            case 0b0101 -> (a >> 1) & limit;
            case 0b0110 -> ((a << 1) | (a >> 7)) & limit;
            case 0b0111 -> ((a >> 1) | (a << 7)) & limit;
            case 0b1000 -> a & b;
            case 0b1001 -> a | b;
            case 0b1010 -> a ^ b;
            case 0b1011 -> ~(a | b) & limit;
            case 0b1100 -> ~(a & b) & limit;
            case 0b1101 -> ~(a ^ b) & limit;
            case 0b1110 -> a > b ? 1 : 0;
            case 0b1111 -> // Equality
                    a == b ? 1 : 0;
            default -> (a + b) & limit;
        };
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
