package com.example.maplander_be.controller;


import com.example.maplander_be.domain.User;
import com.example.maplander_be.dto.EmailCheckResponse;
import com.example.maplander_be.dto.LoginDto;
import com.example.maplander_be.dto.RegisterDto;
import com.example.maplander_be.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;
    public AuthController(UserService svc){
        this.userService = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto dto){

        userService.register(dto);
        return ResponseEntity.ok("회원가입 성공");

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = userService.login(dto.email(), dto.password());

        // 기존 세션이 있으면 무효화
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // 새로운 세션 생성
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("LOGIN_USER", user.getUserId());


//        // 수동 세션 쿠키 내려주기
//        Cookie sessionCookie = new Cookie("JSESSIONID", newSession.getId());
//        sessionCookie.setHttpOnly(true);
//        sessionCookie.setSecure(false);
//        sessionCookie.setPath("/");
//        sessionCookie.setMaxAge(60 * 60);    // 1시간 유지
//        sessionCookie.setAttribute("SameSite", "None");
//        response.addCookie(sessionCookie);

        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }

//        //  클라이언트 쿠키 제거
//        Cookie clearCookie = new Cookie("JSESSIONID", null);
//        clearCookie.setPath("/");
//        clearCookie.setMaxAge(0);
//        response.addCookie(clearCookie);

        // JSESSIONID 쿠키 즉시 삭제
        Cookie clear = new Cookie("JSESSIONID", "");
        clear.setMaxAge(0);
        clear.setHttpOnly(true);
        clear.setSecure(false);
        response.addCookie(clear);

        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        return ResponseEntity.ok("현재 로그인 사용자 ID: " + session.getAttribute("LOGIN_USER"));
    }


    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);

        String message = available
                ? "사용 가능한 이메일입니다."
                : "이미 사용 중인 이메일입니다.";


        return ResponseEntity
                .ok(new EmailCheckResponse(available, message));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}
