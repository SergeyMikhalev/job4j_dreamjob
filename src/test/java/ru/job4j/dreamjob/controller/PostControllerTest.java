package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class PostControllerTest {
    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(
                        1,
                        "New post",
                        "description 1",
                        LocalDateTime.now(),
                        true,
                        new City(1, "Msk")
                ),
                new Post(
                        2,
                        "New post",
                        "description 1",
                        LocalDateTime.now(),
                        true,
                        new City(1, "Msk")
                )
        );
        User regUser = new User(1, "user1", "user1@mail.ru", "pass1");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(regUser);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        verify(model).addAttribute("regUser", regUser);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(
                1,
                "New post",
                "description 1",
                LocalDateTime.now(),
                true,
                new City(1, "Msk"));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }
}