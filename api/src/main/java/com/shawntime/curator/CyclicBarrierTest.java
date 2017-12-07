package com.shawntime.curator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shma on 2017/11/15.
 */
public class CyclicBarrierTest {

    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "准备就绪...");
                        cyclicBarrier.await();
                        System.out.println(Thread.currentThread().getName() + "开始跑步...");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println(Thread.currentThread().getName() + "到达钟点...");
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//            countDownLatch.await();
//            exec.shutdown();
        }
    }
}
