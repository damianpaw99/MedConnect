package edu.ib.repository;

import edu.ib.object.doctor.DoctorDto;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface DoctorDtoRepository extends CrudRepository<DoctorDto,Long> {

    @Procedure(procedureName = "add_doctor")
    void addDoctor(Long pesel, String name, String surname, @Param("birth_date") Date dateOfBirth, @Param("phone")Integer phoneNumber, String email, @Param("password") String hashedPassword);

    @Procedure(procedureName = "assign_spec_to_doctor")
    void addSpecToDoctor(@Param("pesel") Long doctorPesel,@Param("spec_name")String specName);

    @Procedure(procedureName="change_doctor_password")
    void changePassword(@Param("peselinput") Long doctorPesel,@Param("hashedpassword") String newPassword);
}
