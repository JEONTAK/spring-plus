package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserService userService;

    /*@Test
    public void testSearchPerformance() {
        String nickname = "user99999129";
        long startTime = System.currentTimeMillis();
        List<UserSearchResponse> users = userService.findUsersByNickname(nickname);
        long endTime = System.currentTimeMillis();
        System.out.println("Search took: " + (endTime - startTime) + "ms, Results: " + users.size());
    }*/

}