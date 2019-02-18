package io.github.alirezamht.authentication.repository;

import io.github.alirezamht.authentication.model.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session,Long> {

    Session findBySessionString(String s);

    List<Session> findByUserIdentifier(String s);

    @Modifying // needed for updating query
    @Query("UPDATE Session s SET s.expireTime= :expireDate WHERE s.id= :id")
    void update(@Param("expireDate") Long expireDate,@Param("id") Long id);

}
