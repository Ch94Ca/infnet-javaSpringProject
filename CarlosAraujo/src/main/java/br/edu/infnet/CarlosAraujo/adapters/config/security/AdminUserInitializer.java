package br.edu.infnet.CarlosAraujo.adapters.config.security;

import br.edu.infnet.CarlosAraujo.application.port.in.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final AdminService adminService;

    public AdminUserInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) {
        adminService.initializeAdminUser();
    }
}