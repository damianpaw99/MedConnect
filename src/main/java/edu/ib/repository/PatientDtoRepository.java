package edu.ib.repository;

import edu.ib.object.patient.PatientDto;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDtoRepository extends CrudRepository<PatientDto,Long> {

    @Procedure(procedureName="change_patients_password")
    void changePassword(@Param("peselinput") Long patientPesel, @Param("hashedpassword") String newPassword);
}
