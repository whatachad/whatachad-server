package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.type.FacilityType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Slice<Facility> findFacilityByCategory(Pageable pageable, FacilityType category);
}
