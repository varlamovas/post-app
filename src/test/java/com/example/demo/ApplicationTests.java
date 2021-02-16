package com.example.demo;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
import com.example.demo.rest.PostController;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class ApplicationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@SneakyThrows
	@Test
	void testPostCreation() {
		final PostDto dto = new PostDto();
		dto.setTitle("test title");
		dto.setContent("test content");

		final Post post = new Post();
		post.setId(1L);
		post.setTitle(dto.getTitle());
		post.setContent(dto.getContent());
		when(postService.save(refEq(post, "id"))).thenReturn(post);
		mockMvc.perform(
					post("/post").content(objectMapper.writeValueAsString(dto)).contentType("application/json")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(post)));
	}

	@SneakyThrows
	@Test
	void testGetAllPosts() {
		final List<Post> posts = Arrays.asList(
				new Post() {{
					setId(1L);
					setTitle("title 1");
					setContent("content 1");
				}},
				new Post() {{
					setId(2L);
					setTitle("title 2");
					setContent("content 2");
				}}
		);
		when(postService.findAll()).thenReturn(posts);
		mockMvc.perform(
					get("/post").contentType("application/json")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(posts)));
	}

	@SneakyThrows
	@Test
	void testGetPostById() {
		final Post expectedPost = new Post() {{
			setId(1L);
			setTitle("title 1");
			setContent("content 1");
		}};
		final List<Post> posts = Arrays.asList(
				expectedPost,
				new Post() {{
					setId(2L);
					setTitle("title 2");
					setContent("content 2");
				}}
		);
		when(postService.findById(eq(1L))).thenReturn(Optional.of(expectedPost));
		mockMvc.perform(
					get("/post/1").contentType("application/json")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(expectedPost)));
	}

}
