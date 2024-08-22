package task.usermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import task.usermanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    boolean existsByLogin(String login);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id BETWEEN :startId AND :endId")
    void deleteRange(@Param("startId") Long startId, @Param("endId") Long endId);
}
