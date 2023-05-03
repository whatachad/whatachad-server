package com.whatachad.app.repository;

import com.whatachad.app.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select m from User m where m.id <> ?1")
    List<User> findAllExceptOne(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByPhone(String phone);

    void deleteByEmail(String email);

    @Query("select m from User m, Follow f1 where m.id in " +
            "(select f2.followingId from Follow f2 where f2.follower.id = :loginId)")
    List<User> findFollowingUser(@Param("loginId") String loginId);
}
