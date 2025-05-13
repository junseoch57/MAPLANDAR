package com.example.maplander_be.controller;

import com.example.maplander_be.domain.Friend;
import com.example.maplander_be.dto.FriendDto;
import com.example.maplander_be.dto.FriendResponseDto;
import com.example.maplander_be.service.FriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService svc;
    public FriendController(FriendService svc) {
        this.svc = svc; }

    @PostMapping("/request")
    public ResponseEntity<String> sendRequest(
            @RequestBody FriendDto dto,
            HttpSession session
    ) {
        // 1) 세션에서 로그인된 사용자 ID 꺼내기
        Integer me = (Integer) session.getAttribute("LOGIN_USER");
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        // 2) 서비스에 (ID, 이메일) 전달
        svc.sendRequest(me, dto.getReceiverEmail());
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }

    // 수정: 대기 중인 친구 요청을 DTO 형태로 반환하도록 변경
    @GetMapping("/pending")
    public ResponseEntity<?> pending(HttpSession session) {
        Integer me = (Integer) session.getAttribute("LOGIN_USER");
        if (me == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "errorCode", "UNAUTHORIZED",
                            "message", "로그인이 필요합니다."
                    ));
        }

        List<FriendResponseDto> data = svc.getPendingRequestsDto(me);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "data", data
        ));
    }

    @PostMapping("/accept")
    public ResponseEntity<String> accept(@RequestBody FriendDto dto) {
        svc.acceptRequest(dto.getRequesterId(), dto.getReceiverId());
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

    @PostMapping("/decline")
    public ResponseEntity<String> decline(@RequestBody FriendDto dto) {
        svc.declineRequest(dto.getRequesterId(), dto.getReceiverId());
        return ResponseEntity.ok("친구 요청을 거절했습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(HttpSession session) {
        Integer me = (Integer) session.getAttribute("LOGIN_USER");
        if (me == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "errorCode", "UNAUTHORIZED",
                            "message", "로그인이 필요합니다."
                    ));
        }
        List<FriendResponseDto> data = svc.getFriendList(me);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "data", data
        ));
    }


}
