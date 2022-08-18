package com.hanghea99.minispring.service;

import com.hanghea99.minispring.model.*;
import com.hanghea99.minispring.model.dto.ArticleIdDto;
import com.hanghea99.minispring.model.dto.ArticleRequestDto;
import com.hanghea99.minispring.model.dto.ArticleResponseDto;
import com.hanghea99.minispring.model.dto.memeDto;
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
    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final CommentRepository commentRepository;

    private  final HeartRepository heartRepository;

    //Error 공유 게시글 생성
    public ArticleIdDto createArticle(ArticleRequestDto articleRequestDto) {
        Member member = memberService.getSigningUser();
        Article article = new Article(articleRequestDto, member);

        switch (articleRequestDto.getLanguage()) {
            case "JAVA":
                article.setLanguage(Language.JAVA);
                break;
            case "JS":
                article.setLanguage(Language.JS);
                break;
            case "PYTHON":
                article.setLanguage(Language.PYTHON);
                break;
            default:
                article.setLanguage(Language.NULL);
                break;
        }

        member.addArticle(article);
        articleRepository.save(article);
        return new ArticleIdDto(article);
    }

    //전체게시물 조회
    public List<ArticleResponseDto> readAllArticle() {
        List<Article> articleList = articleRepository.findAllByOrderByCreatedAtDesc();
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
    public List<ArticleResponseDto> readAllJava(){
        List<Article> articleList = articleRepository.findAllByLanguage(Language.JAVA);
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
                articleResponseDtoList.add(new ArticleResponseDto(article));
        }
        return articleResponseDtoList;
    }

    //파이썬게시물
    public List<ArticleResponseDto> readAllPython(){
        List<Article> articleList = articleRepository.findAllByLanguage(Language.PYTHON);
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
                articleResponseDtoList.add(new ArticleResponseDto(article));
        }
        return articleResponseDtoList;
    }

    //JS 게시물
    public List<ArticleResponseDto> readAllJs(){
        List<Article> articleList = articleRepository.findAllByLanguage(Language.JS);
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList){
                articleResponseDtoList.add(new ArticleResponseDto(article));
        }
        return articleResponseDtoList;
    }


    //게시물 업데이트
    @Transactional
    public Long updateArticle(Long id, ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Member member = memberService.getSigningUser();  //로그인 한 유저만 수정할 수있으니까

        if(member.getUsername().equals(article.getUsername())){
            article.updateArticle(articleRequestDto);
            return id;
        }else return 0L;
    }

    //게시물 지우기
    public Long deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Member member = memberService.getSigningUser();

        if(member.getUsername().equals(article.getUsername())){
            member.removeArticle(article);
            articleRepository.delete(article);
            return id;
        }else return 0L;
    }

    //게시글 좋아요
    public String heartArticle(Long articleId) {
        Member member = memberService.getSigningUser();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

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

    @Transactional
	public String meme(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Member member = memberService.getSigningUser(); // 로그인 맴버

        int cnt = 4;

        if(!article.getMemberId().contains(member.getId())){
            if (article.getMemberId().size() < cnt){
                article.addMeme(member.getId());
                article.setMemeCnt(article.getMemberId().size());
                return member.getUsername() + "님 등록완료! 총 인원 : " + article.getMemberId().size() + "남은 모집 인원 :" + (cnt-article.getMemberId().size()) +"명";
            }
            return "모집인원이 초과되었습니다 총 모집인원 : "+cnt+"명"+" / 현재 모집인원"+ article.getMemberId().size();
        }else {
            article.removeMeme(member.getId());
            article.setMemeCnt(article.getMemberId().size());
            return member.getUsername() + "님 등록취소! 총 인원 : " + article.getMemberId().size() + "남은 모집 인원 :" + (cnt-article.getMemberId().size()) +"명";
        }
	}

    public memeDto mememe(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        memeDto memeDto = new memeDto(article);
        for (int i=0; i<article.getMemeCnt(); i++){
            Member member = memberRepository.findById(article.getMemberId().get(i))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
            memeDto.add(member);
        }

        return memeDto;
    }
}
