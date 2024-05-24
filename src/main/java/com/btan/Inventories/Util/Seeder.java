package com.btan.Inventories.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.btan.Inventories.model.Role;
import com.btan.Inventories.repo.RoleRepository;

import jakarta.transaction.Transactional;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        if (roleRepository.findByName("ADMINISTRATOR").isEmpty()) {
            Role developerRole = new Role();
            developerRole.setName("ADMINISTRATOR");
            developerRole.setDescription("Site Administrator Role");
            roleRepository.save(developerRole);
        }

        if (roleRepository.findByName("STAFF").isEmpty()) {
            Role managerRole = new Role();
            managerRole.setName("STAFF");
            managerRole.setDescription("The normal Employee Role with Limited access");
            roleRepository.save(managerRole);
        }
    }
}
