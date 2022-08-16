package com.hanghea99.minispring.service;

import com.hanghea99.minispring.model.dto.CommentRequestDto;
import com.hanghea99.minispring.model.dto.CommentResponseDto;
import com.hanghea99.minispring.model.Article;
import com.hanghea99.minispring.model.Comment;
import com.hanghea99.minispring.model.Heart;
import com.hanghea99.minispring.model.Member;
import com.hanghea99.minispring.repository.ArticleRepository;
import com.hanghea99.minispring.repository.CommentRepository;
import com.hanghea99.minispring.repository.HeartRepository;
import com.hanghea99.minispring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private  final HeartRepository heartRepository;

    //comment 작성하기
    public Comment creatComment(Long articleId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getSigningUser();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto, article, member);


        member.addComment(comment);
        article.addComment(comment);

        commentRepository.save(comment);
        return comment;

    }

    //comment 불러오기
    public List<CommentResponseDto> readAllComment(Long articleId) {
        List<Comment> commentList = commentRepository.findAllByArticle_Id(articleId);
        List<CommentResponseDto> commentResponseList = new ArrayList<>();

        for(Comment comment : commentList){
            commentResponseList.add(new CommentResponseDto(comment));
        }
        return commentResponseList;
    }

    //comment 수정하기
    @Transactional
    public Long updateComment(Long commentId, CommentRequestDto commentRequestDto) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(()-> new NullPointerException("해당 아이디가 존재하지 않습니다."));
    Member member = memberService.getSigningUser();

		if(member.getUsername().equals(comment.getUsername())){
        comment.updateComment(commentRequestDto);
        return 0L;
    }else return commentId;
}

    //comment 삭제하기
    public Long deleteComment(Long commentId) {
        Member member = memberService.getSigningUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        Article article = articleRepository.findById(comment.getArticle().getId())
                .orElseThrow(()-> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if(member.getUsername().equals(comment.getUsername())){
            member.removeComment(comment);
            article.removeComment(comment);
            commentRepository.delete(comment);
            return 0L;
        }else return commentId;
    }

    //댓글 좋아요
    public String heartComment(Long commentId) {
        Member member = memberService.getSigningUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if(heartRepository.findByMemberAndComment(member, comment) == null){
            Heart heart = new Heart(member, comment);
            member.addHeart(heart);
            comment.addHeart(heart);
            comment.setHeartCnt(comment.getHeartList().size());
            heartRepository.save(heart);
            return comment.getId() + "번 댓글 좋아요" + ", 총 좋아요 수 : " + comment.getHeartCnt();
        }else  {
            Heart heart = heartRepository.findByMemberAndComment(member, comment);
            member.removeHeart(heart);
            comment.removeHeart(heart);
            comment.setHeartCnt(comment.getHeartList().size());
            heartRepository.delete(heart);
            return comment.getId() + "번 댓글 좋아요 취소" + ", 총 좋아요 수: " + comment.getHeartCnt();
        }
    }

    @Transactional
    public String selectedComment(Long commentId) {
        Member member = memberService.getSigningUser();
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(()-> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        Article article = comment.getArticle();

        if (member.getUsername().equals(article.getUsername())){
            article.setSelectedComment(commentId);
            return commentId + "번 댓글 채택 완료";
        }else return "게시물 작성자만 채택할 수 있습니다.";
    }
}
