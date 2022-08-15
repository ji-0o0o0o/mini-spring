package com.hanghea99.minispring.controller;

import com.hanghea99.minispring.model.dto.ArticleRequestDto;
import com.hanghea99.minispring.model.dto.ArticleResponseDto;
import com.hanghea99.minispring.model.Article;
import com.hanghea99.minispring.model.dto.ArticleIdDto;
import com.hanghea99.minispring.service.ArticleService;
import com.hanghea99.minispring.service.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "*", allowedHeaders = "*")
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;
    private final S3Uploader s3Uploader;


    //Error 공유 게시글 생성
    @PostMapping("")
    public Article createArticle(@RequestBody ArticleRequestDto articleRequestDto){
        return articleService.createArticle(articleRequestDto);
    }

    //이미지 등록
    @PostMapping("/image/{articleId}")
    public String upload(@PathVariable Long articleId, MultipartFile multipartFile, String dirName) throws IOException {
        return s3Uploader.upload(articleId, multipartFile, "img");
    }

    //전체 게시물 조회
    @GetMapping("")
    public List<ArticleResponseDto> readAllPost(){
        return articleService.readAllArticle();
    }

    //게시글 상세 페이지
    @GetMapping("{articleId}")
    public ArticleIdDto readArticleId(@PathVariable Long articleId){
        return articleService.readArticleId(articleId);
    }
    //자바 게시물

    //파이썬게시물

    //JS 게시물

    //게시물 업데이트
    @PatchMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, @RequestBody ArticleRequestDto articleRequestDto){
        return articleService.updateArticle(articleId, articleRequestDto);
    }
    //게시물 지우기
    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId){
        return  articleService.deleteArticle(articleId);
    }

    //게시글 좋아요
    @PostMapping("/heart/{articleId}")
    private String heartArticle(@PathVariable Long articleId){
        return articleService.heartArticle(articleId);
    }
}



