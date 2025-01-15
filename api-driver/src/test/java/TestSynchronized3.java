import java.util.Random;

/**
 * 结论：并不是所有的在方法上加synchronized就能保证同步执行
 */
public class TestSynchronized3 {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            MyThread2 thread = new MyThread2();
            thread.start();
        }
    }
}

class MyThread3 extends Thread {
    public void run() {
        SynchronizedDemo3 synchronizedDemo = new SynchronizedDemo3();
        synchronizedDemo.sync();
    }
}

class SynchronizedDemo3 {
    public void sync() {
        synchronized ("") {
            int i = new Random().nextInt(20);
            System.out.println("线程开始：" + i);
            System.out.println("执行业务逻辑" + i);
            System.out.println("线程结束：" + i);
        }

    }
}
