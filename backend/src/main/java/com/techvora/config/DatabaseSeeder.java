package com.techvora.config;

import com.techvora.entity.Role;
import com.techvora.entity.User;
import com.techvora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.techvora.repository.AuditLogRepository auditLogRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Run column migrations to ensure PostgreSQL text columns are of type TEXT (BLOB/CLOB)
        try {
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN cover_image TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN content TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN excerpt TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN image_alt_text TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN seo_description TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE articles ALTER COLUMN canonical_url TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE audit_logs ALTER COLUMN details TYPE TEXT");
            System.out.println("PostgreSQL columns migrated to TEXT successfully.");
        } catch (Exception e) {
            System.out.println("Column alter migration note: " + e.getMessage());
        }

        User adminUser = null;
        // Seed an admin account if it doesn't exist
        Optional<User> existingAdmin = userRepository.findByEmail("admin@techvora.com");
        
        if (existingAdmin.isEmpty()) {
            adminUser = User.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email("admin@techvora.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
                    
            adminUser = userRepository.save(adminUser);
            System.out.println("=========================================================");
            System.out.println("Default Admin Account Created!");
            System.out.println("Email: admin@techvora.com");
            System.out.println("Password: admin123");
            System.out.println("=========================================================");
        } else {
            adminUser = existingAdmin.get();
        }

        // Seed initial audit log entries if audit_logs table is empty
        if (auditLogRepository.count() == 0) {
            auditLogRepository.save(com.techvora.entity.AuditLog.builder()
                    .user(adminUser)
                    .action("SYSTEM_INIT")
                    .entityId("sys-001")
                    .details("Techvora Spring Boot backend system initialized on Neon PostgreSQL.")
                    .build());
            
            auditLogRepository.save(com.techvora.entity.AuditLog.builder()
                    .user(adminUser)
                    .action("ADMIN_CREATED")
                    .entityId(adminUser.getId().toString())
                    .details("Super Admin account seeded: admin@techvora.com")
                    .build());
        }
    }
}
