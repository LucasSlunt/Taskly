package com.example.task_manager.service;

import java.security.SecureRandom;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TeamMemberRepository;



@Service
public class AuthInfoService {

    protected final TeamMemberRepository teamMemberRepository;
    
        // Constructor for required repositories
        public AuthInfoService(TeamMemberRepository teamMemberRepository){
            this.teamMemberRepository = teamMemberRepository;
    }

    public boolean approveLogin(int teamMemberId, String enteredPassword) {
        boolean isSuccess = false;
        //TeamMember teamMember = teamMemberRepository.findbyUserName(username);
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));
        String TMHashedPassword = teamMember.getAuthInfo().getHashedPassword();
        String TMSalt = teamMember.getAuthInfo().getSalt();
        String enteredHashedPassword = AuthInfoService.hashPassword(enteredPassword, TMSalt);
        if (enteredHashedPassword == TMHashedPassword) {isSuccess = true;}
        return isSuccess;
    }

    public static String hashPassword(String plainTextPassword, String salt){
        /*This method takes a user and the password that the user enters to log into their 
          account, and returns the password hash associated with that user and password. 
        */
        //String salt = teamMember.getAuthInfo().getSalt();
        Argon2PasswordEncoder argon2id = new Argon2PasswordEncoder(0, 32, 1, 60000, 12);
        String HashedPassword = argon2id.encode(plainTextPassword.concat(salt));
        return HashedPassword;
    }

    public static String generateSalt(){
        /*
         * Uses the SecureRandom class to generate a cryptographically secure random 
         * string of bytes, which is returned as a string
         */
        SecureRandom secureRandom = new SecureRandom();
        int lengthOfSalt = 16;
        byte[] salt = new byte[lengthOfSalt];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }
}
