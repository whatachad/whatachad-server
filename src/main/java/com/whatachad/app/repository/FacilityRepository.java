package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.FindFacilityDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f from Facility f where f.address.jibunAddress like %:area%")
    Slice<Facility> findByArea(Pageable pageable, @Param("area") String area);

    @Query("select f from Facility f where f.address.regionCode = :regionCode")
    Slice<Facility> findByRegionCode(Pageable pageable, @Param("regionCode") String regionCode);

    @Query(value = "select * from Facility f"
            + " where ST_DWithin(CAST(ST_SetSRID(ST_Point(:#{#dto.latitude}, :#{#dto.longitude}), 4326) as"
            + " geography), CAST(ST_SetSRID(ST_Point(f.latitude, f.longitude), 4326) as"
            + " geography), :#{#dto.distance})"
            + " and f.category = :#{#dto.category.name()}",
            nativeQuery = true)
    Slice<Facility> findFaciliteisAroundUser(Pageable pageable, @Param("dto") FindFacilityDto dto);

}
