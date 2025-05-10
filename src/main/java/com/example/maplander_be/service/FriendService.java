package com.example.maplander_be.service;

import com.example.maplander_be.domain.Friend;
import com.example.maplander_be.domain.FriendId;
import com.example.maplander_be.domain.User;
import com.example.maplander_be.repository.FriendRepository;
import com.example.maplander_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendService {

    private final FriendRepository repo;
    private final UserRepository userRepo;

    public FriendService(FriendRepository repo, UserRepository userRepo){
        this.repo = repo;
        this.userRepo = userRepo;
    }

    // 친구 요청 전송
    @Transactional
    public void sendRequest(Integer requesterId, String receiverEmail) {
        // 1) 로그인 정보가 없으면 예외
        if (requesterId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // 2) 이메일로 User 엔티티 조회
        User receiver = userRepo.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("요청 대상 이메일을 가진 사용자가 없습니다."));

        Integer receiverId = receiver.getUserId();

        // 3) 자신에게 요청 금지
        if (requesterId.equals(receiverId)) {
            throw new IllegalArgumentException("자기 자신에게는 요청할 수 없습니다.");
        }

        // 4) 중복 요청 방지
        FriendId id = new FriendId(requesterId, receiverId);
        if (repo.existsById(id)) {
            throw new IllegalArgumentException("이미 요청된 사용자입니다.");
        }

        // 5) 새 요청 저장
        repo.save(new Friend(requesterId, receiverId, false));
    }

    // 친구 요청 수락 (기존 그대로)
    @Transactional
    public void acceptRequest(Integer requesterId, Integer receiverId){
        FriendId id = new FriendId(requesterId, receiverId);
        Friend fr = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청이 존재하지 않습니다."));
        fr.setAccepted(true);
        repo.save(fr);
    }

    // 친구 요청 거절
    @Transactional
    public void declineRequest(Integer requesterId, Integer receiverId){
        FriendId id = new FriendId(requesterId, receiverId);
        if (!repo.existsById(id)){
            throw new IllegalArgumentException("요청이 존재하지 않습니다.");
        }
        repo.deleteById(id);
    }

    // 대기 중인 요청 조회
    public List<Friend> getPendingRequests(Integer userId) {
        return repo.findByIdReceiverIdAndAcceptedFalse(userId);
    }

    // 친구 목록 조회
    public List<Friend> getFriends(Integer userId) {
        return repo.findByIdRequesterIdOrIdReceiverIdAndAcceptedTrue(userId, userId);
    }


}
