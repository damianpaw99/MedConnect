package edu.ib.repository;

import edu.ib.object.DoctorDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDtoRepository extends CrudRepository<DoctorDto,Long> {
}
