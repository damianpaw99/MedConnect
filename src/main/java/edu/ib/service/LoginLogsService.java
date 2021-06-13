package edu.ib.service;


import edu.ib.object.LoginLog;
import edu.ib.repository.LoginLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogsService {

    private LoginLogsRepository loginLogsRepository;

    @Autowired
    public LoginLogsService(LoginLogsRepository loginLogsRepository) {
        this.loginLogsRepository = loginLogsRepository;
    }

    public void addLog(LoginLog logs){
        loginLogsRepository.save(logs);
    }
}
