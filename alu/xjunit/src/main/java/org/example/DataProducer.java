package org.example;

import java.util.Random;

public class DataProducer {
    private final int NUM_LIMIT = 0xFF;

    private final int NUM_BOUND = NUM_LIMIT + 1;

    private final int LIMIT = 16;

    private final Random random = new Random();

    int a = random.nextInt(NUM_BOUND);
    int b = random.nextInt(NUM_BOUND);

    int sel = random.nextInt(LIMIT);

    int out = refModel(a, b, sel);


    private int refModel(int a, int b, int sel){
        switch (sel) {
            case 0b0000: return  (a + b) & NUM_LIMIT;
            case 0b0001: return  (a - b) & NUM_LIMIT;
            case 0b0010: return  (a * b) & NUM_LIMIT;
            case 0b0011: return b != 0 ? a / b : 0;
            case 0b0100: return (a << 1) & NUM_LIMIT;
            case 0b0101: return (a >> 1) & NUM_LIMIT;
            case 0b0110: return ((a << 1) | (a >> 7)) & NUM_LIMIT;
            case 0b0111: return ((a >> 1) | (a << 7)) & NUM_LIMIT;
            case 0b1000: return a & b;
            case 0b1001: return a | b;
            case 0b1010: return a ^ b;
            case 0b1011: return ~(a | b) & NUM_LIMIT;
            case 0b1100: return ~(a & b) & NUM_LIMIT;
            case 0b1101: return ~(a ^ b) & NUM_LIMIT;
            case 0b1110: return a > b ? 1 : 0;
            case 0b1111: // Equality
                return a == b ? 1 : 0;
            default:
                return (a + b) & NUM_LIMIT;
        }

    }


    void produce(){
        a = random.nextInt(NUM_LIMIT);
        b = random.nextInt(NUM_LIMIT);
        sel = random.nextInt(LIMIT);
        out = refModel(a, b, sel);
    }
}
