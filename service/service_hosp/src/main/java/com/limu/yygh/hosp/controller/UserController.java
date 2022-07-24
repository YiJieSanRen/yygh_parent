package com.limu.yygh.hosp.controller;

import com.limu.yygh.common.result.R;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @PostMapping("login")
    public R login(@RequestBody User user) {
        if ("admin".equals(user.getUsername()) && "111111".equals(user.getPassword())) {
            return R.ok().data("token", "admin-token");
        }
        return R.error().code(60204).message("Account and password are incorrect");
    }

    @GetMapping("info")
    public R info() {
        return R.ok().data("roles", "[admin]")
                .data("introduction", "I am a super administrator")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .data("name", "Super Admin");
    }
}
