package com.cqupt.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author zsw
 * @create 2023-07-14 0:06
 */
public class BlockingQueueTests {
    public static void main(String[] args) {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
        new  Thread(new Producer(queue)).start();
        new  Thread(new Comsummer(queue)).start();
        new  Thread(new Comsummer(queue)).start();
        new  Thread(new Comsummer(queue)).start();
    }


}
class  Producer implements  Runnable{
    private BlockingQueue<Integer> queue;
    public  Producer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName()+"生产:"+queue.size());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class  Comsummer implements  Runnable{
    private BlockingQueue<Integer> queue;
    public   Comsummer(BlockingQueue<Integer> queue){
        this.queue=queue;
    }
    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(new Random().nextInt(1000)+100);
                queue.take();
                System.out.println(Thread.currentThread().getName()+"消费:"+queue.size());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
