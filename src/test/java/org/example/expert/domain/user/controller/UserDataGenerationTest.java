package org.example.expert.domain.user.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserDataGenerationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@Test
    public void generateMillionUsers() throws SQLException {
        long startTime = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO `spring-plus`.users (email, password, nickname, user_role) VALUES (?, ?, ?, ?)")) {
            conn.setAutoCommit(false);
            int batchSize = 1000;
            Random random = new Random();
            String password = "!password";
            String encodedPassword = passwordEncoder.encode(password);
            for (int i = 1; i < 1_000_000; i++) {
                String nickname = "user" + i + random.nextInt(1000);
                String email = "user" + i + "@example.com";
                String userRole = (i % 10 == 0) ? "ROLE_ADMIN" : "ROLE_USER";
                pstmt.setString(1, email);
                pstmt.setString(2, encodedPassword);
                pstmt.setString(3, nickname);
                pstmt.setString(4, userRole);
                pstmt.addBatch();

                if (i % batchSize == 0) {
                    System.out.println("Save USER " + i);
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            conn.commit();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Data generation took: " + (endTime - startTime) + "ms");
    }*/
}