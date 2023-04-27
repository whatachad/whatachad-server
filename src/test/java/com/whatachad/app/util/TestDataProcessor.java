package com.whatachad.app.util;

import com.whatachad.app.init.TestInit;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Component
public class TestDataProcessor {

    @Autowired
    private EntityManager em;
    @Autowired
    private PlatformTransactionManager txManager;

    public TestDataProcessor(EntityManager em, PlatformTransactionManager txManager) {
        this.em = em;
        this.txManager = txManager;
    }

    public void rollback() {
        TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
        em.createQuery("delete from Facility f where f.title not in :title")
                .setParameter("title", List.of(TestInit.FACILITY_TITLE))
                .executeUpdate();
        txManager.commit(txStatus);
    }
}
