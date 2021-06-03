package edu.ib.repository;

import edu.ib.object.appointment.AllAppointmentView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllAppointmentsViewRepository extends CrudRepository<AllAppointmentView,Long> {
}
