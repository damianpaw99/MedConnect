package edu.ib.repository;

import edu.ib.object.patient.PatientDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDtoRepository extends CrudRepository<PatientDto,Long> {
}
