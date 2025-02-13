package com.yeojeong.application.domain.member.application;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.board.board.application.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.application.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.config.email.EmailManager;
import com.yeojeong.application.domain.member.domain.RedisAuthed;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.planner.location.application.LocationService;
import com.yeojeong.application.domain.planner.location.domain.Location;
import com.yeojeong.application.domain.planner.planner.application.PlannerService;
import com.yeojeong.application.domain.planner.planner.domain.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberFacade {
    private final int AUTH_TIME = 5 * 60;
    private final String AUTH_CHECK_KEY = "authKey";

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final PlannerService plannerService;
    private final LocationService locationService;

    private final EmailManager emailManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisAuthedService redisAuthedService;

    public MemberResponse.FindById findById(Long id) {
        Member savedEntity = memberService.findById(id);
        return MemberResponse.FindById.toDto(savedEntity);
    }

    @Transactional
    public void save(MemberRequest.MemberSave dto) {
        if(!redisAuthedService.checkKey(dto.email(), AUTH_CHECK_KEY)) throw new AuthedException("인증이 되지 않은 사용자 입니다.");
        redisAuthedService.delete(dto.email());

        String encodingPassword = passwordEncoder.encode(dto.password());
        Member entity = MemberRequest.MemberSave.toEntity(dto, encodingPassword);
        memberService.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        Member savedEntity = memberService.findById(id);
        if(!redisAuthedService.checkKey(savedEntity.getUsername(), AUTH_CHECK_KEY)) throw new AuthedException("인증이 되지 않은 사용자 입니다.");
        redisAuthedService.delete(savedEntity.getUsername());

        memberService.delete(savedEntity);
    }

    @Transactional
    public void update(Long id, MemberRequest.MemberPut dto) {
        Member savedEntity = memberService.findById(id);
        if(!redisAuthedService.checkKey(savedEntity.getUsername(), AUTH_CHECK_KEY)) throw new AuthedException("사전 인증이 되지 않은 사용자 입니다.");

        if(!savedEntity.getNickname().equals(dto.nickname()))
            memberService.checkDuplicatedByNickname(dto.nickname());

        Member updateEntity = MemberRequest.MemberPut.toEntity(dto);
        memberService.update(savedEntity, updateEntity);
        redisAuthedService.delete(savedEntity.getUsername());
    }

    @Transactional
    public void updatePassword(Long id, MemberRequest.PatchPassword dto) {
        if(!dto.password().equals(dto.checkPassword())) throw new AuthedException("비밀번호가 일치하지 않습니다.");

        Member savedEntity = memberService.findById(id);
        if(!redisAuthedService.checkKey(savedEntity.getUsername(), AUTH_CHECK_KEY))
            throw new AuthedException("사전 인증이 되지 않은 사용자 입니다.");

        memberService.updatePassword(savedEntity, passwordEncoder.encode(dto.password()));
        redisAuthedService.delete(savedEntity.getUsername());
    }

    @Transactional
    public void findPassword(String username, String email) {
        Member savedEntity = memberService.findByUsername(username);
        if(!savedEntity.getEmail().equals(email))
            throw new NotFoundDataException("입력 값이 일치하지 않습니다.");

        String newPassword = createNewPassword();
        emailManager.findPassword(email, newPassword);

        savedEntity.updatePassword(passwordEncoder.encode(newPassword));
        memberService.updatePassword(savedEntity, passwordEncoder.encode(newPassword));
    }

    private String createNewPassword() {
        String password = "";
        for(int i = 0; i < 4; i++) {
            password += (char) ((int) (Math.random() * 26) + 97);
        }
        for(int i = 0; i < 4; i++) {
            password += (int) (Math.random() * 10);
        }
        return password;
    }

    public void checkPassword(Long id, String password) {
        Member savedEntity = memberService.findById(id);
        if(!passwordEncoder.matches(password, savedEntity.getPassword()))
            throw new AuthedException("비밀번호가 일치하지 않습니다.");

        redisAuthedService.save(RedisAuthed.builder()
                .id(savedEntity.getUsername())
                .value(AUTH_CHECK_KEY)
                .ttl(AUTH_TIME)
                .build());
    }

    public Page<MemberResponse.MemberBoardInfo> findBoardById(Long id, int page) {
        Page<Board> savedBoardPage = boardService.findByMember(id, page);
        return savedBoardPage.map(MemberResponse.MemberBoardInfo::toDto);
    }

    public Page<MemberResponse.MemberCommentInfo> findCommentById(Long id, int page) {
        final int pageSize = 10;

        Member entity = memberService.findById(id);
        List<Comment> savedCommentList = entity.getComments();
        if(savedCommentList.isEmpty()) return new PageImpl<>(new ArrayList<>());

        Map<Long, MemberResponse.MemberCommentInfo> duplicatedBoardEntries = new HashMap<>();
        for(Comment target : savedCommentList) {
            if(duplicatedBoardEntries.containsKey(target.getBoard().getId())) {
                MemberResponse.MemberCommentInfo data = duplicatedBoardEntries.get(target.getBoard().getId());
                data.appendComment(target);
                continue;
            }

            MemberResponse.MemberCommentInfo data = MemberResponse.MemberCommentInfo.createMemberCommentInfo(target);
            duplicatedBoardEntries.put(target.getBoard().getId(), data);
        }

        List<MemberResponse.MemberCommentInfo> rtnList = new ArrayList<>(duplicatedBoardEntries.values());

        if (page < 0) {
            throw new IllegalArgumentException("Page number must be 0 or greater");
        }

        int totalSize = rtnList.size();
        int totalPages = (int) Math.ceil((double) totalSize / pageSize);

        if (page >= totalPages) {
            page = totalPages - 1;
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rtnList.size());

        List<MemberResponse.MemberCommentInfo> pageContent = new ArrayList<>(rtnList.subList(start, end));
        return new PageImpl<>(pageContent, pageable, totalSize);
    }

    public Page<MemberResponse.MemberPlannerInfo> findPlannerById(Long id, int page) {
        Page<Planner> savedPlannerPage = plannerService.findByMemberId(id, page);
        return savedPlannerPage.map(MemberResponse.MemberPlannerInfo::toDto);
    }

    public List<MemberResponse.MemberLocationInfo> findLocationByDate(Long id, Long start, Long end) {
        List<Location> savedLocationList = locationService.findByMemberAndDate(id, start, end);

        Map<Long, Boolean> duplicatedPlannerEntries = new HashMap<>();
        List<MemberResponse.MemberLocationInfo> rtnList = new ArrayList<>();
        for(Location target : savedLocationList) {
            if(duplicatedPlannerEntries.containsKey(target.getPlanner().getId())) continue;
            duplicatedPlannerEntries.put(target.getPlanner().getId(), true);
            rtnList.add(MemberResponse.MemberLocationInfo.toDto(target));
        }

        return rtnList;
    }

    public void findUsernameByEmail(String email) {
        Member savedEntity = memberService.findByEmail(email);
        emailManager.findUsername(email, savedEntity.getUsername());
    }

    public void checkDuplicatedByUsername(String username) {
        memberService.checkDuplicatedByUsername(username);
    }

    public void checkDuplicatedByNickname(String nickname) {
        memberService.checkDuplicatedByNickname(nickname);
    }

    public void checkDuplicatedByEmail(String email) {
        memberService.checkDuplicatedByEmail(email);

        String authKey = getAuthedKey();
        emailManager.emailAuth(email, authKey);
        redisAuthedService.save(RedisAuthed.builder()
                .id(email)
                .value(authKey)
                .ttl(AUTH_TIME)
                .build());
    }

    public void checkAuthCheck(String email, String authKey) {
        if(!redisAuthedService.checkKey(email, authKey))
            throw new AuthedException("인증 코드가 올바르지 않습니다.");

        redisAuthedService.delete(email);
        redisAuthedService.save(RedisAuthed.builder()
                .id(email)
                .value(AUTH_CHECK_KEY)
                .ttl(AUTH_TIME)
                .build());
    }

    private String getAuthedKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }
}
