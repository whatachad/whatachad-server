package com.whatachad.app.util;

import com.whatachad.app.init.TestInit;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TestDataProcessor {

    @Autowired
    private EntityManager em;

    public TestDataProcessor(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void rollback() {
        em.createQuery("delete from Facility f where (f.title not in :title) or (f.title is null)")
                .setParameter("title", List.of(TestInit.FACILITY_TITLE))
                .executeUpdate();
    }
}
