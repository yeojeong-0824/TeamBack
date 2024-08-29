package com.example.demo.board.comment.application;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.board.domain.BoardRepository;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.board.comment.domain.CommentRepository;
import com.example.demo.board.comment.presentation.dto.CommentRequest;
import com.example.demo.board.comment.presentation.dto.CommentResponse;
import com.example.demo.config.exception.AuthorityException;
import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.exception.RequestDataException;
import com.example.demo.config.util.customannotation.MethodTimer;
import com.example.demo.config.util.customannotation.RedissonLocker;
import com.example.demo.member.member.domain.Member;
import com.example.demo.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    @MethodTimer(method = "댓글 작성")
    @RedissonLocker(key = "saveComment")
    public void save(CommentRequest.Save takenDto, Long takenBoardId, Long takenMemberId) {
        Board savedBoard = boardRepository.findById(takenBoardId)
                .orElseThrow(() -> new NotFoundDataException("해당 게시글을 찾을 수 없습니다"));
        Member savedMember = memberRepository.findById(takenMemberId)
                .orElseThrow(() -> new NotFoundDataException("해당 유저를 찾을 수 없습니다"));

        savedBoard.commentCountUp();
        boardRepository.save(savedBoard);

        Comment entity = CommentRequest.Save.toEntity(takenDto, savedBoard, savedMember);
        commentRepository.save(entity);
    }

    @Override
    @MethodTimer(method = "게시글에 작성된 댓글 조회")
    public Page<CommentResponse.FindByBoardId> findByBoardId(Long takenBoardId, int page) {
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<Comment> commentList = commentRepository.findAllByBoardId(takenBoardId, request);

        return toDtoPage(commentList);
    }

    @Override
    @Transactional
    @MethodTimer(method = "댓글 수정")
    public void updateById(Long commentId, Long takenBoardId, Long takenMemberId, CommentRequest.Edit editDto){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다"));

        if(!takenMemberId.equals(comment.getMember().getId())){
            throw new AuthorityException("게시글을 작성한 회원이 아닙니다");
        }

        comment.update(editDto);
    }

    @Override
    @MethodTimer(method = "댓글 삭제")
    @RedissonLocker(key = "deleteComment")
    public void deleteById(Long commentId, Long takenMemberId){
        Comment savedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundDataException("해당 댓글을 찾을 수 없습니다"));

        if(!takenMemberId.equals(savedComment.getMember().getId())){
            throw new AuthorityException("게시글을 작성한 회원이 아닙니다");
        }

        Board savedBoard = savedComment.getBoard();
        savedBoard.commentCountDown();
        boardRepository.save(savedBoard);

        commentRepository.delete(savedComment);
    }

    public Page<CommentResponse.FindByBoardId> toDtoPage(Page<Comment> commentList){
        return commentList.map(CommentResponse.FindByBoardId::toDto);
    }
}
