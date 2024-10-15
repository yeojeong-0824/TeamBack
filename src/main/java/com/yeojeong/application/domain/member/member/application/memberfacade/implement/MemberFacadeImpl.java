package com.yeojeong.application.domain.member.member.application.memberfacade.implement;

import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.config.exception.handler.ErrorCode;
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
    public MemberResponse.FindMember findById(Long id) {
        Member savedEntity = memberService.findById(id);
        return MemberResponse.FindMember.toDto(savedEntity);
    }

    @Override
    public void save(MemberRequest.SaveMember dto) {
        String encodingPassword = passwordEncoder.encode(dto.password());
        Member entity = MemberRequest.SaveMember.toEntity(dto, encodingPassword);
        memberService.save(entity);
    }

    @Override
    public void delete(long id, MemberRequest.DeleteMember dto) {
        Member savedEntity = memberService.findById(id);

        String savedPassword = savedEntity.getPassword();
        String takenPassword = dto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException(ErrorCode.PASSWORD_NOT_MATCH);

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
    public String findPassword(String username, String email) {
        Member savedEntity = memberService.findByUsername(username);
        if(!savedEntity.getEmail().equals(email)) throw new NotFoundDataException(ErrorCode.NOT_FOUND_EMAIL);

        String newPassword = this.createNewPassword();
        savedEntity.patchPassword(passwordEncoder.encode(newPassword));

        memberService.pathPassword(savedEntity, newPassword);
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

    @Transactional
    @Override
    public MemberResponse.FindMember patch(Long id, MemberRequest.PatchMember dto) {
        Member savedEntity = memberService.findById(id);

        if(dto.password() != null) {
            String newPassword = passwordEncoder.encode(dto.password());
            memberService.pathPassword(savedEntity, newPassword);
        }

        Member entity = MemberRequest.PatchMember.toEntity(dto);
        Member rtnEntity = memberService.patch(entity, entity);

        return MemberResponse.FindMember.toDto(rtnEntity);
    }
}
