import java.util.Random;

/**
 * 结论：并不是所有的synchronized所this就能保证同步执行
 */
public class TestSynchronized2 {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            MyThread2 thread = new MyThread2();
            thread.start();
        }
    }
}

class MyThread2 extends Thread {
    public void run() {
        SynchronizedDemo2 synchronizedDemo = new SynchronizedDemo2();
        synchronizedDemo.sync();
    }
}

class SynchronizedDemo2 {
    public void sync() {
        synchronized (this) {
            int i = new Random().nextInt(20);
            System.out.println("线程开始：" + i);
            System.out.println("执行业务逻辑" + i);
            System.out.println("线程结束：" + i);
        }

    }
}
