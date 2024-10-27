package com.yeojeong.application.domain.member.application.memberfacade.implement;

import com.yeojeong.application.config.exception.AuthedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.config.email.EmailManager;
import com.yeojeong.application.domain.member.application.emailauthservice.EmailAuthService;
import com.yeojeong.application.domain.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.domain.EmailAuth;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {
    private final String AUTH_CHECK_KEY = "authKey";

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    private final EmailManager emailManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthService emailAuthService;

    @Override
    public MemberResponse.FindById findById(Long id) {
        Member savedEntity = memberService.findById(id);
        return MemberResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public void save(MemberRequest.SaveMember dto) {
        if(!emailAuthService.checkAuthKey(dto.email(), AUTH_CHECK_KEY)) throw new AuthedException("인증이 되지 않은 사용자 입니다.");
        emailAuthService.delete(dto.email());

        String encodingPassword = passwordEncoder.encode(dto.password());
        Member entity = MemberRequest.SaveMember.toEntity(dto, encodingPassword);
        memberService.save(entity);
    }

    @Override
    @Transactional
    public void delete(long id, MemberRequest.Delete dto) {
        Member savedEntity = memberService.findById(id);

        String savedPassword = savedEntity.getPassword();
        String takenPassword = dto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다.");

        memberService.delete(savedEntity);
    }

    @Override
    @Transactional
    public MemberResponse.FindById patch(Long id, MemberRequest.Patch dto) {
        Member savedEntity = memberService.findById(id);

        if(dto.password() != null) {
            String newPassword = passwordEncoder.encode(dto.password());
            savedEntity.patchPassword(newPassword);
        }

        Member entity = MemberRequest.Patch.toEntity(dto);
        savedEntity.patchMember(entity);
        Member rtnEntity = memberService.patch(entity);

        return MemberResponse.FindById.toDto(rtnEntity);
    }

    @Override
    public Page<MemberResponse.BoardInfo> findBoardById(long id, int page) {
        Page<Board> savedBoardPage = boardService.findByMember(id, page);
        return savedBoardPage.map(MemberResponse.BoardInfo::toDto);
    }

    @Override
    public Page<MemberResponse.CommentInfo> findCommentById(long id, int page) {
        Page<Comment> savedCommentPage = commentService.findByMemberId(id, page);
        return savedCommentPage.map(MemberResponse.CommentInfo::toDto);
    }


    @Override
    @Transactional
    public void findPassword(String username, String email) {
        Member savedEntity = memberService.findByUsername(username);
        if(!savedEntity.getEmail().equals(email)) throw new NotFoundDataException("입력 값이 일치하지 않습니다.");

        String newPassword = createNewPassword();
        emailManager.findPassword(email, newPassword);

        savedEntity.patchPassword(passwordEncoder.encode(newPassword));
        memberService.patch(savedEntity);
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

    @Override
    public void findUsernameByEmail(String email) {
        Member savedEntity = memberService.findByEmail(email);
        emailManager.findUsername(email, savedEntity.getUsername());
    }

    @Override
    public void checkDuplicatedByUsername(String username) {
        memberService.checkDuplicatedByUsername(username);
    }
    @Override
    public void checkDuplicatedByNickname(String nickname) {
        memberService.checkDuplicatedByNickname(nickname);
    }

    @Override
    public void checkDuplicatedByEmail(String email) {
        memberService.checkDuplicatedByEmail(email);

        String authKey = getAuthKey();
        emailManager.emailAuth(email, authKey);
        emailAuthService.save(EmailAuth.builder()
                .id(email)
                .value(authKey)
                .ttl(5 * 60)
                .build());
    }

    @Override
    public void checkAuthCheck(String email, String authKey) {
        if(!emailAuthService.checkAuthKey(email, authKey)) throw new AuthedException("인증 코드가 올바르지 않습니다.");
        emailAuthService.delete(email);
        emailAuthService.save(EmailAuth.builder()
                .id(email)
                .value(AUTH_CHECK_KEY)
                .ttl(5 * 60)
                .build());
    }

    private String getAuthKey() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int rInt = random.nextInt(10000);
        return String.format("%04d", rInt);
    }
}
