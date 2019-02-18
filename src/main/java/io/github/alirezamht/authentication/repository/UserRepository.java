package io.github.alirezamht.authentication.repository;

import io.github.alirezamht.authentication.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

   // @Query(value = "SELECT s from User s WHERE s.username = :username")
    List<User> findUserByUsername(@Param("username")String username);
   // ArrayList<User> getAllBy();

}
