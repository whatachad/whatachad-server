package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Re_Daywork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Re_DayworkRepository extends JpaRepository<Re_Daywork, Long> {

}
