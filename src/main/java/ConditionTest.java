import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wangliang on 10/11/15.
 */
public class ConditionTest {

    public static void main(String[] args) {
//        new A().run();
        new B().run();
    }


}

class B {
    private Object lock = new Object();

    public void run() {
        synchronized (lock) {
            try {
                System.out.println("main run");
                new MyThread().start();
                System.out.println("main wait");
                lock.wait(20000);
                System.out.println("main wake");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("t1 run");
                new MyThread2().start();
                Thread.sleep(5000);
                System.out.println("t1 week");
                lock.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyThread2 extends Thread {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println("t2 run");
                    Thread.sleep(1000);
                    System.out.println("t3 week, notifiy all");
                    lock.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class A {
    private final ReentrantLock mNettylock = new ReentrantLock();
    private final Condition mCondition = mNettylock.newCondition();

    public void run() {
        mNettylock.lock();
        try {
            new MyThread().start();
            Thread.currentThread().sleep(5000);
            System.out.println("get up");
            mCondition.await();
            System.out.println("not wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mNettylock.unlock();
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Mythread run");
            mNettylock.lock();
            new MyThread2().start();

            try {
                mCondition.await();
                Thread.currentThread().sleep(5000);
                System.out.println("Mythread not wait");
                mCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mNettylock.unlock();
            }

        }
    }

    private class MyThread2 extends Thread {
        @Override
        public void run() {
            mNettylock.lock();
            System.out.println("Mythread2 run");
            try {
                mCondition.signal();

            } finally {
                mNettylock.unlock();
            }

        }
    }
}
/*
/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/bin/java -Didea.launcher.port=7534 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA 14 CE.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/javafx-doclet.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/lib/tools.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/htmlconverter.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Users/wangliang/IdeaProjects/ConditionTest/build/classes/main:/Users/wangliang/IdeaProjects/ConditionTest/build/resources/main:/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain ConditionTest
Mythread run
Exception in thread "Thread-1" java.lang.IllegalMonitorStateException
get up
	at java.util.concurrent.locks.ReentrantLock$Sync.tryRelease(ReentrantLock.java:155)
Mythread2 run
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.release(AbstractQueuedSynchronizer.java:1260)
	at java.util.concurrent.locks.ReentrantLock.unlock(ReentrantLock.java:460)
	at A$MyThread2.run(ConditionTest.java:61)
not wait

Process finished with exit code 0
* */