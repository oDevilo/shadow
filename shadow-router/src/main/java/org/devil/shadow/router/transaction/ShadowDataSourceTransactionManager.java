package org.devil.shadow.router.transaction;

import org.devil.shadow.router.exception.ShadowMultipleException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.*;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by devil on 2017/10/20.
 */
public class ShadowDataSourceTransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {
    private Set<DataSource> shards;
    protected List<AbstractPlatformTransactionManager> transactionManagers;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (shards == null || shards.isEmpty()) throw new IllegalArgumentException("'shards' is required.");
        transactionManagers = new ArrayList<>();
        /**
         * 需要设置这个为2 SYNCHRONIZATION_NEVER 而不使用默认的 SYNCHRONIZATION_ALWAYS 使得事务同步不激活
         * 否则会产生 Cannot activate transaction synchronization - already active @see TransactionSynchronizationManager.initSynchronization()
         *
         * newTransaction  的作用，比较容易明白，标识该切面方法是否新建了事务，后续切面方法执行完毕时，通过该字段判断是否 需要提交事务或者回滚事务
         * newSynchronization  与transactionSynchronization有关系
         * transactionSynchronization 的作用就是你不需要事务的时候，也可以使你整个业务执行完毕再关闭connection,不用每执行一个dml语句都去申请connection
         */
        setTransactionSynchronization(SYNCHRONIZATION_NEVER);
        for (DataSource shard : shards) {
            DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(shard);
            transactionManager.setDefaultTimeout(getDefaultTimeout());
            transactionManager.setTransactionSynchronization(getTransactionSynchronization());
            transactionManagers.add(transactionManager);
        }
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        return new ArrayList<DefaultTransactionStatus>();
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        List<TransactionStatus> statusList = (List<TransactionStatus>) transaction;
        for (AbstractPlatformTransactionManager transactionManager : transactionManagers) {
            statusList.add(transactionManager.getTransaction(definition));
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        ShadowMultipleException ex = new ShadowMultipleException();
        List<TransactionStatus> statusList = (List<TransactionStatus>) status.getTransaction();
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            AbstractPlatformTransactionManager transactionManager = transactionManagers.get(i);
            try {
                transactionManager.commit(statusList.get(i));
            } catch (TransactionException e) {
                ex.add(e);
            }
        }
        if (!ex.getCauses().isEmpty())
            throw new HeuristicCompletionException(HeuristicCompletionException.STATE_UNKNOWN, ex);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        ShadowMultipleException ex = new ShadowMultipleException();
        List<TransactionStatus> statusList = (List<TransactionStatus>) status.getTransaction();
        for (int i = transactionManagers.size() - 1; i >= 0; i--) {
            AbstractPlatformTransactionManager transactionManager = transactionManagers.get(i);
            try {
                transactionManager.rollback(statusList.get(i));
            } catch (TransactionException e) {
                ex.add(e);
            }
        }
        if (!ex.getCauses().isEmpty())
            throw new UnexpectedRollbackException("one or more error on rolling back the transaction", ex);
    }

    public Set<DataSource> getShards() {
        return shards;
    }

    public void setShards(Set<DataSource> shards) {
        this.shards = shards;
    }
}
