package edu.ib.repository;

import edu.ib.object.LoginLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogsRepository extends CrudRepository<LoginLog,Long> {
}
