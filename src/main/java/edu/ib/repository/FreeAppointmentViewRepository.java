package edu.ib.repository;

import edu.ib.object.appointment.FreeAppointmentView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeAppointmentViewRepository extends CrudRepository<FreeAppointmentView, Long> {

    @Query(value = "select f from FreeAppointmentView f where  f.surname like %:keyword% or f.name like %:keyword% or f.specs like %:keyword%")
    List<FreeAppointmentView> findByKeyword(@Param("keyword") String keyword);
}
