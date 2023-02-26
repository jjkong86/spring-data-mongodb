package com.me.springdata.mongodb.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorUtil {
    private final ExecutorService executorService;
    private final List<Future<?>> list = new ArrayList<>();

    public ExecutorUtil(int threadCount) {
//        NamedThreadsFactory factory = new NamedThreadsFactory();
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void supportExecute(Callable<Void> callable) {
        this.list.add(this.executorService.submit(callable)); // 로직처리후 return 값을 담음
    }

    public void supportExecute(Runnable runnable) {
        this.list.add(this.executorService.submit(runnable));
    }

    public void supportExecuteWithOutFuture(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    /*
     * callable을 활용하여 동기 처리
     *  - 동기처리를 각 로직마다 하면 안됨(순차 처리 되기 때문에 멀티 쓰레드 이점이 없음)
     *  - 결과값(Future)을 list에 담은 뒤 결과값을 체크함(이 시점에서 동기 처리됨)
     *  - Exception이 발생하면 즉시 main thread에 Exception을 전파하여 에러 처리함
     * */
    public void executorWaitAndShutdown() {
        executorService.shutdown();
        try {
            // 처리중인 작업이 종료 되지 않으면 강제 종료 해줌
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        if (!CollectionUtils.isEmpty(list)) list.forEach(ExecutorUtil::accept);
    }

    private static void accept(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
