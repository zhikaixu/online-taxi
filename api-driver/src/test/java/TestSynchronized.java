import java.util.Random;

/**
 * 结论：并不是所有的在方法上加synchronized就能保证同步执行
 */
public class TestSynchronized {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            MyThread thread = new MyThread();
            thread.start();
        }
    }
}

class MyThread extends Thread {
    public void run() {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        synchronizedDemo.sync();
    }
}

class SynchronizedDemo {
    public synchronized void sync() {
        int i = new Random().nextInt(20);
        System.out.println("线程开始：" + i);
        System.out.println("执行业务逻辑" + i);
        System.out.println("线程结束：" + i);
    }
}
