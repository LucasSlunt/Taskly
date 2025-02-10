package com.example.task_manager.service;

import org.springframework.stereotype.Service;

import com.example.task_manager.repository.AuthInfoRepository;

@Service
public class AuthInfoService {
    private AuthInfoRepository authInfoRepository;

    public void hashPassword() {}

    public boolean approveLogin() {
        boolean isSuccess = false;

        //logic for approving login

        return isSuccess;
    }
}
