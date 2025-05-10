package com.example.maplander_be.controller;


import com.example.maplander_be.domain.User;
import com.example.maplander_be.dto.EmailCheckResponse;
import com.example.maplander_be.dto.LoginDto;
import com.example.maplander_be.dto.RegisterDto;
import com.example.maplander_be.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService svc;
    public AuthController(UserService svc){
        this.svc = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto dto){

        svc.register(dto);
        return ResponseEntity.ok("회원가입 성공");

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto, HttpSession session) {
        User user = svc.login(dto.email(), dto.password());
        session.setAttribute("LOGIN_USER", user.getUserId());
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(HttpSession session) {
        Object id = session.getAttribute("LOGIN_USER");
        if (id == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        return ResponseEntity.ok("현재 로그인 사용자 ID: " + id);
    }


    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean ok = svc.isEmailAvailable(email);
        if (!ok) {
            return ResponseEntity
                    .badRequest()
                    .body(new EmailCheckResponse(false, "이미 사용 중인 이메일입니다."));
        }
        return ResponseEntity
                .ok(new EmailCheckResponse(true, "사용 가능한 이메일입니다."));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}
