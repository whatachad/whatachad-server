package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Daywork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayworkRepository extends JpaRepository<Daywork, Long> {

}
