package edu.ib.service;

import edu.ib.object.AllResultsView;
import edu.ib.object.Result;
import edu.ib.repository.AllResultsViewRepository;
import edu.ib.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ResultService{

    private ResultRepository resultRepository;
    private AllResultsViewRepository allResultsViewRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository, AllResultsViewRepository allResultsViewRepository) {
        this.resultRepository = resultRepository;
        this.allResultsViewRepository=allResultsViewRepository;
    }


    public void addResult(Result result){
        resultRepository.save(result);
    }

    public Iterable<AllResultsView> getAllViewResults(){
        return allResultsViewRepository.findAll();
    }
    public Iterable<AllResultsView> getPatientResults(Long pesel){
        return allResultsViewRepository.findByPatientPesel(pesel);
    }
}
