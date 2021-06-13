package edu.ib.repository;

import edu.ib.object.appointment.FreeAppointmentView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeAppointmentViewRepository extends CrudRepository<FreeAppointmentView, Long> {
}
