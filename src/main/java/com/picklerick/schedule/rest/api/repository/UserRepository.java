package com.picklerick.schedule.rest.api.repository;

import com.picklerick.schedule.rest.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User>findByManagerId(Long managerId);

}
