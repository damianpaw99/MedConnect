package edu.ib.repository;

import edu.ib.object.EmployeeDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDtoRepository extends CrudRepository<EmployeeDto,Long> {
}
