package edu.ib.service;

import edu.ib.object.Result;
import edu.ib.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService{

    private ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }


    public void addResult(Result result){
        resultRepository.save(result);
    }
}
