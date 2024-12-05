package org.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class FIFOTest {
    private FIFOWrapper fifo;
    private FIFORef ref;
    private static int FULL = 8;
    private static int EMPTY = 0;
    @Before
    public void setUp() {
        fifo = new FIFOWrapper();
        ref = new FIFORef();
    }

    @Test
    public void testFIFO() {
        int cur = 0;
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 100000; i++){
            int choice = random.nextInt(3);
            boolean write = (cur == EMPTY || (cur < FULL && choice > 0));
            int rand = random.nextInt(256);
            if (write) {
                FIFOWriteRet expect = fifo.write(rand);
                FIFOWriteRet actual = ref.write(rand);
                assertEquals(expect, actual);
                cur++;
            } else {
                FIFOReadRet expect = fifo.read();
                FIFOReadRet actual = ref.read();
                assertEquals(expect, actual);
                cur--;
            }
        }
    }


    private class FIFORef{
        private final Queue<Integer> queue = new ArrayDeque<>();

        public FIFOWriteRet write(int in){
            FIFOWriteRet ret = new FIFOWriteRet(queue.isEmpty()?1:0, queue.size() == FULL? 1: 0);
            if (queue.size() < FULL){
                queue.offer(in);
            }
            return ret;
        }

        public FIFOReadRet read(){
            assert !queue.isEmpty();
            int res = queue.poll();
            return new FIFOReadRet(res, queue.isEmpty()?1:0, queue.size() == FULL? 1: 0);
        }
    }
}
