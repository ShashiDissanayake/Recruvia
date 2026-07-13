package com.recruvia.backend.config;

import com.recruvia.backend.entity.Role;
import com.recruvia.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole(
                "ADMIN",
                "System Administrator with full system access."
        );

        createRole(
                "RECRUITER",
                "Recruiter who manages companies and job postings."
        );

        createRole(
                "CANDIDATE",
                "Candidate who applies for jobs."
        );
    }

    private void createRole(String name, String description) {

        if (!roleRepository.existsByName(name)) {

            Role role = new Role();
            role.setName(name);
            role.setDescription(description);

            roleRepository.save(role);

            System.out.println("Created Role : " + name);

        } else {

            System.out.println("Role already exists : " + name);

        }
    }

}