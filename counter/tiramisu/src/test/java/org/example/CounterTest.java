package org.example;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.Random;

public class CounterTest {

    private CounterDriver counter;
    private CounterRef ref;
    @Before
    public void setUp() {
        counter = new CounterDriver();
        ref = new CounterRef();
        ref.reset();
        counter.reset();
    }

    @After
    public void tearDown() {
        counter.finish();
    }

    @Test
    public void randTest() {
        counter.reset();
        ref.reset();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 100000; i++){
            int chosen = rand.nextInt(100);
            if (chosen < 31){
                ref.reset();
                counter.reset();
            } else {
                assertEquals(ref.tick(), counter.tick());
            }
        }
    }

    private static class CounterRef{

        private int count = 0;

        public void reset(){
            count = 0;
        }

        public int tick(){
            int res = count;
            count++;
            count &= 0xF;
            return res;
        }

    }
}
