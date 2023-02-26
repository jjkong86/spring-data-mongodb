package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.utils.ExecutorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class UserTotalTransactionService {
    private final UserTransactionService userTransactionService;
    private final UserDetailTransactionService userDetailTransactionService;

    @Transactional
    public void requiredNewTest(Long userId, boolean rollback) {
        userTransactionService.userLocListUpdate(userId);
        userDetailTransactionService.newTransactionTest(userId, false);
        if (rollback) throw new RuntimeException("User locList update rollback. userId : " + userId);
    }

    @Transactional
    public void requiredNewRollbackTest(Long userId, boolean rollback) {
        userTransactionService.userLocListUpdate(userId);
        userDetailTransactionService.newTransactionTest(userId, true);
        if (rollback) throw new RuntimeException("User locList update rollback. userId : " + userId);
    }

    public void readOnlyTest(Long userId) {
        ExecutorUtil executorUtil = new ExecutorUtil(2);

        executorUtil.supportExecute(() -> userTransactionService.readOnlyTest(userId));
        executorUtil.supportExecute(() -> userTransactionService.userUpdate(userId));
        executorUtil.executorWaitAndShutdown();
    }

    public void WriteConflictTest(Long userId) {
        ExecutorUtil executorUtil = new ExecutorUtil(2);

        executorUtil.supportExecute(() -> userTransactionService.writeConflictTest(userId));
        executorUtil.supportExecute(() -> userTransactionService.userUpdate(userId));
        executorUtil.executorWaitAndShutdown();
    }

}
