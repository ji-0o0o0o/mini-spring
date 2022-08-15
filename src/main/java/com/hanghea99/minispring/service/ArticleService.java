package com.hanghea99.minispring.service;

import com.hanghea99.minispring.model.*;
import com.hanghea99.minispring.model.dto.ArticleRequestDto;
import com.hanghea99.minispring.model.dto.ArticleResponseDto;
import com.hanghea99.minispring.model.dto.ArticleIdDto;
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
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    private final CommentRepository commentRepository;

    private  final HeartRepository heartRepository;

    //Error 공유 게시글 생성
    public Article createArticle(ArticleRequestDto articleRequestDto) {
        Member member = memberService.getSigningUser();
        Article article = new Article(articleRequestDto, member);

        if (articleRequestDto.getLanguage().equals("JAVA")) {
            article.setLanguage(Language.JAVA);
        } else if (articleRequestDto.getLanguage().equals("JS")) {
            article.setLanguage(Language.JS);
        } else if (articleRequestDto.getLanguage().equals("PYTHON")){
            article.setLanguage(Language.PYTHON);
        }else if (articleRequestDto.getLanguage() == null){
            article.setLanguage(Language.NULL);
        }else article.setLanguage(Language.NULL);

        member.addArticle(article);
        articleRepository.save(article);
        return article;
    }

    public List<ArticleResponseDto> readAllJava(){
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
            if(article.getLanguage().equals(Language.JAVA)){
                articleResponseDtoList.add(new ArticleResponseDto(article));
            }
        }
        return articleResponseDtoList;
    }

    public List<ArticleResponseDto> readAllJs(){
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
            if(article.getLanguage().equals(Language.JS)){
                articleResponseDtoList.add(new ArticleResponseDto(article));
            }
        }
        return articleResponseDtoList;
    }

    public List<ArticleResponseDto> readAllPython(){
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
            if(article.getLanguage().equals(Language.PYTHON)){
                articleResponseDtoList.add(new ArticleResponseDto(article));
            }
        }
        return articleResponseDtoList;
    }

    //전체게시물 조회
    public List<ArticleResponseDto> readAllArticle() {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article:articleList) {
            articleResponseDtoList.add(new ArticleResponseDto(article));
        }
        return articleResponseDtoList;
    }

    //상세 게시물 조회
    public ArticleIdDto readArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        ArticleIdDto articleIdDto = new ArticleIdDto(article);
        if (article.getIsDone()){
            Comment comment = commentRepository.findById(article.getSelectedCommentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
            articleIdDto.setSelectedComment(comment);
        }


        return articleIdDto;
    }

    //자바 게시물

    //파이썬게시물

    //JS 게시물

    //게시물 업데이트
    @Transactional
    public String updateArticle(Long id, ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Member member = memberService.getSigningUser();  //로그인 한 유저만 수정할 수있으니까

        if(member.getUsername().equals(article.getUsername())){
            article.updateArticle(articleRequestDto);
            return "수정 성공";
        }else return "수정 실패";
    }

    //게시물 지우기
    public String deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Member member = memberService.getSigningUser();

        if(member.getUsername().equals(article.getUsername())){
            member.removeArticle(article);
            articleRepository.delete(article);
            return "삭제 성공";
        }else return "삭제 실패";
    }

    //게시글 좋아요
    public String heartArticle(Long articleId) {
        Member member = memberService.getSigningUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if(heartRepository.findByMemberAndArticle(member, article) == null){
            Heart heart = new Heart(member, article);
            member.addHeart(heart);
            article.addHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.save(heart);
            return article.getId() + "번 게시물 좋아요" + ", 총 좋아요 수 : " + article.getHeartCnt();
        }else  {
            Heart heart = heartRepository.findByMemberAndArticle(member, article);
            member.removeHeart(heart);
            article.removeHeart(heart);
            article.setHeartCnt(article.getHeartList().size());
            heartRepository.delete(heart);
            return article.getId() + "번 게시물 좋아요 취소" + ", 총 좋아요 수 : " + article.getHeartCnt();
        }
    }


}
