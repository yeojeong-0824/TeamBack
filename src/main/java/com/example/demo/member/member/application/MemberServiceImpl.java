package com.example.demo.member.member.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.domain.CommentRepository;
import com.example.demo.config.exception.RequestDataException;
import com.example.demo.config.util.methodtimer.MethodTimer;
import com.example.demo.config.exception.DuplicatedException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import com.example.demo.member.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.save()")
    public void save(MemberRequest.SaveMember takenMemberRequest) {
        String encodingPassword = passwordEncoder.encode(takenMemberRequest.password());
        Member takenMember = MemberRequest.SaveMember.toEntity(takenMemberRequest, encodingPassword);
        memberRepository.save(takenMember);
    }

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.deleteByUserId")
    public void deleteByMemberId(Long takenMemberId, MemberRequest.DeleteMember takenDto) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        String savedPassword = savedEntity.getPassword();
        String takenPassword = takenDto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다");

        boardRepository.deleteByMember(savedEntity);
        memberRepository.deleteById(takenMemberId);
    }

    @Override
    @MethodTimer(method = "MemberService.findById()")
    public MemberResponse.FindMember findById(Long takenMemberId) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return MemberResponse.FindMember.toDto(savedEntity);
    }

    @Override
    public Page<MemberResponse.BoardInfo> findBoardById(Long takenMemberId, int takenPage) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        PageRequest pageRequest = PageRequest.of(takenPage - 1, 10, Sort.by("id").descending());
        Page<Board> savedBoardPage = boardRepository.findByMember(savedEntity, pageRequest);

        return savedBoardPage.map(MemberResponse.BoardInfo::toDto);
    }

    @Override
    public Page<MemberResponse.CommentInfo> findCommentById(Long takenMemberId, int takenPage) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        PageRequest pageRequest = PageRequest.of(takenPage - 1, 10, Sort.by("id").descending());
        Page<Comment> savedComment = commentRepository.findByMember(savedEntity, pageRequest);

        return savedComment.map(MemberResponse.CommentInfo::toDto);
    }

    @Override
    @MethodTimer(method = "MemberService.createNewPassword()")
    public String createNewPassword(String takenUsername, String takenEmail) {
        Member savedEntity = memberRepository.findByUsername(takenUsername)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        if(!savedEntity.getEmail().equals(takenEmail))
            throw new NotFoundDataException("해당 유저의 이메일을 찾지 못했습니다");

        String newPassword = this.createNewPassword();
        savedEntity.patchPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(savedEntity);

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
    @MethodTimer(method = "MemberService.findUsernameByEmail()")
    public String findUsernameByEmail(String takenEmail) {
        Member savedEntity = memberRepository.findByEmail(takenEmail)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return savedEntity.getUsername();
    }

    @Override
    @MethodTimer(method = "MemberService.checkDuplicated()")
    public void checkDuplicated(MemberRequest.DataConfirmMember takenDto) {
        String takenNickname = takenDto.nickname();
        String takenUsername = takenDto.username();

        if(memberRepository.existsByNickname(takenNickname))
            throw new DuplicatedException("중복된 닉네임입니다");

        if(memberRepository.existsByUsername(takenUsername))
            throw new DuplicatedException("중복된 아이디입니다");
    }

    @Override
    @MethodTimer(method = "MemberService.checkDuplicatedByEmail()")
    public void checkDuplicatedByEmail(String takenEmail) {
        if(memberRepository.existsByEmail(takenEmail))
            throw new DuplicatedException("중복된 이메일입니다");
    }

    @Transactional
    @Override
    @MethodTimer(method = "MemberService.patchPasswordByUsername()")
    public void patchById(Long takenMemberId, MemberRequest.PatchMember takenDto) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        String savedPassword = savedEntity.getPassword();
        String takenPassword = takenDto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다");

        String newPassword = takenDto.newPassword() == null ? null : passwordEncoder.encode(takenDto.newPassword());
        savedEntity.patchMember(takenDto, newPassword);
        memberRepository.save(savedEntity);
    }
}
