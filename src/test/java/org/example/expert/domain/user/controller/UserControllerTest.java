package org.example.expert.domain.user.controller;

import java.util.List;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.SecurityConfig;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({SecurityConfig.class, JwtUtil.class})
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSearchPerformance() {
        String nickname = "user99999129";
        long startTime = System.currentTimeMillis();
        List<UserSearchResponse> users = userService.findUsersByNickname(nickname);
        long endTime = System.currentTimeMillis();
        System.out.println("Search took: " + (endTime - startTime) + "ms, Results: " + users.size());
    }

}