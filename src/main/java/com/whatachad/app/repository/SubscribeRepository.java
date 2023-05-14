package com.whatachad.app.repository;

import com.whatachad.app.model.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Follow,Long> {

    @Query("select case when count(f) > 0 then true else false end from Follow f " +
            "where f.follower.id = :followerId and f.followingId = :followingId")
    boolean existsFollow(@Param("followerId") String followerId,@Param("followingId") String followingId);

    @Query("select f from Follow f where f.follower.id = :followerId and f.followingId = :followingId")
    Optional<Follow> findByFollow(@Param("followerId") String followerId, @Param("followingId") String followingId);
}
