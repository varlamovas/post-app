package com.example.demo.rest;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable("id") Long id) {
        return postService.findById(id).orElse(null);
    }

    @GetMapping("/post")
    public List<Post> getPosts() {
        return postService.findAll();
    }

    @PostMapping("/post")
    public Post createPost(@RequestBody PostDto postDto) {
        final Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return postService.save(post);
    }

}
