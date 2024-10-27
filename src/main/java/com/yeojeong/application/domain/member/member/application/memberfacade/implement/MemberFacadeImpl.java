package com.yeojeong.application.domain.member.member.application.memberfacade.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.domain.board.board.application.boardservice.BoardService;
import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.comment.application.commentservice.CommentService;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.member.member.application.memberfacade.MemberFacade;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse.FindById findById(Long id) {
        Member savedEntity = memberService.findById(id);
        return MemberResponse.FindById.toDto(savedEntity);
    }

    @Override
    @Transactional
    public void save(MemberRequest.SaveMember dto) {
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
    public String findPassword(String username, String email) {
        Member savedEntity = memberService.findByUsername(username);
        if(!savedEntity.getEmail().equals(email)) throw new NotFoundDataException("입력 값이 일치하지 않습니다.");

        String newPassword = this.createNewPassword();
        savedEntity.patchPassword(passwordEncoder.encode(newPassword));

        memberService.patch(savedEntity);
        return newPassword;
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
    public String findUsernameByEmail(String email) {
        Member savedEntity = memberService.findByEmail(email);
        return savedEntity.getUsername();
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
}
