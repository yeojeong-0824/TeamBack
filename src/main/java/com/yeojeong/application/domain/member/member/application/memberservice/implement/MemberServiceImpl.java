package com.yeojeong.application.domain.member.member.application.memberservice.implement;

import com.yeojeong.application.domain.board.board.domain.Board;
import com.yeojeong.application.domain.board.board.domain.BoardRepository;
import com.yeojeong.application.domain.board.comment.domain.Comment;
import com.yeojeong.application.domain.board.comment.domain.CommentRepository;
import com.yeojeong.application.config.exception.RequestDataException;
import com.yeojeong.application.config.exception.DuplicatedException;
import com.yeojeong.application.config.exception.NotFoundDataException;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberResponse;
import com.yeojeong.application.domain.member.member.application.memberservice.MemberService;
import com.yeojeong.application.domain.member.member.presentation.dto.MemberRequest;
import com.yeojeong.application.domain.member.member.domain.Member;
import com.yeojeong.application.domain.member.member.domain.MemberRepository;
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

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));
    }

    @Transactional
    @Override
    public void save(MemberRequest.SaveMember takenDto) {
        String encodingPassword = passwordEncoder.encode(takenDto.password());
        Member takenMember = MemberRequest.SaveMember.toEntity(takenDto, encodingPassword);
        memberRepository.save(takenMember);
    }

    @Transactional
    @Override
    public void deleteByMemberId(Long takenMemberId, MemberRequest.DeleteMember takenDto) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        String savedPassword = savedEntity.getPassword();
        String takenPassword = takenDto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다");

        boardRepository.deleteByMember(savedEntity);
        commentRepository.deleteByMember(savedEntity);
        memberRepository.deleteById(takenMemberId);
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
    public String findPassword(String takenUsername, String takenEmail) {
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
    public String findUsernameByEmail(String takenEmail) {
        Member savedEntity = memberRepository.findByEmail(takenEmail)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        return savedEntity.getUsername();
    }

    @Override
    public void checkDuplicatedByUsername(String takenUsername) {
        if(memberRepository.existsByUsername(takenUsername))
            throw new DuplicatedException("중복된 아이디입니다");
    }
    @Override
    public void checkDuplicatedByNickname(String takenNickname) {
        if(memberRepository.existsByNickname(takenNickname))
            throw new DuplicatedException("중복된 닉네임입니다");
    }

    @Override
    public void checkDuplicatedByEmail(String takenEmail) {
        if(memberRepository.existsByEmail(takenEmail))
            throw new DuplicatedException("중복된 이메일입니다");
    }

    @Transactional
    @Override
    public MemberResponse.FindMember patchById(Long takenMemberId, MemberRequest.PatchMember takenDto) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        String savedPassword = savedEntity.getPassword();
        String takenPassword = takenDto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다");

        savedEntity.patchMember(takenDto);
        Member save = memberRepository.save(savedEntity);

        return MemberResponse.FindMember.toDto(save);
    }

    @Transactional
    @Override
    public MemberResponse.FindMember patchPasswordById(Long takenMemberId, MemberRequest.PatchPassword takenDto) {
        Member savedEntity = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾지 못했습니다"));

        String savedPassword = savedEntity.getPassword();
        String takenPassword = takenDto.password();
        if(!passwordEncoder.matches(takenPassword, savedPassword)) throw new RequestDataException("비밀번호가 일치하지 않습니다");

        String password = passwordEncoder.encode(takenDto.newPassword());
        savedEntity.patchPassword(password);
        Member save = memberRepository.save(savedEntity);

        return MemberResponse.FindMember.toDto(save);
    }
}
