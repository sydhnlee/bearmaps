package bearmaps.utils.pq;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void test1() {
        MinHeapPQ test = new MinHeapPQ();
        NaiveMinPQ test2 = new NaiveMinPQ();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int randy = rand.nextInt(10);
            test.insert(i, randy);
            test2.insert(i, randy);
        }

        for (int i = 0; i < 10; i++) {
            int decider = rand.nextInt(2) + 1;
            if (decider == 1) {
                System.out.println("Mine: " + test.poll() + "\tCorrect: " + test2.poll());
            }
            else {
                int randy = rand.nextInt(10);
                int randy2 = rand.nextInt(10);
                test.changePriority(randy2, randy);
                test2.changePriority(randy2, randy);
            }
        }

        //System.out.println(test);
        //System.out.println(test2.toString());
        int size = test.size();
        /*
        for (int i = 0; i < size; i++) {
            Object testPoll = test.poll();
            Object test2Poll = test2.poll();
            System.out.println("Mine: " + testPoll + "\tCorrect: " + test2Poll);
            //System.out.println(test);
            //System.out.print("Test hash: " + testPoll.hashCode() + "Test 2 hash: " + test2Poll.hashCode());
        }*/


    }

    @Test
    public void test2() {
        Random ok = new Random();
        System.out.println(ok.nextInt(2) + 1);

    }
}
