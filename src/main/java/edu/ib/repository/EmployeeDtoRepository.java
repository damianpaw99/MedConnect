package edu.ib.repository;

import edu.ib.object.employee.EmployeeDto;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDtoRepository extends CrudRepository<EmployeeDto,Long> {

    @Procedure(procedureName="change_employees_password")
    void changePassword(@Param("peselinput") Long employeePesel, @Param("hashedpassword") String newPassword);
}
