package hello.Controller;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTask {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
}
