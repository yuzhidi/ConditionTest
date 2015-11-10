import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wangliang on 10/11/15.
 */
public class ConditionTest {

    public static void main(String[] args) {
        new A().run();
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

    class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Mythread run");
            mNettylock.lock();
            new MyThread2().start();
            try {
                Thread.currentThread().sleep(5000);
                mCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mNettylock.unlock();
            }

        }
    }

    class MyThread2 extends Thread {
        @Override
        public void run() {
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