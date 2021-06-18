package edu.ib.repository;

import edu.ib.object.AllResultsView;
import edu.ib.object.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllResultsViewRepository extends CrudRepository<AllResultsView,Long> {

    Iterable<AllResultsView> findByPatientPesel(Long pesel);
}
