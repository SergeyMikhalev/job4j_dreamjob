package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    private final PostService postService;
    private final CityService cityService;
    private final HttpSession session;
    private final User regUser;
    private final Model model;

    public PostControllerTest() {
        postService = mock(PostService.class);
        cityService = new CityService();
        session = mock(HttpSession.class);
        regUser = new User(1, "user1", "user1@mail.ru", "pass1");
        when(session.getAttribute("user")).thenReturn(regUser);
        model = mock(Model.class);
    }

    @AfterEach
    public void resetMocks() {
        reset(postService);
        reset(model);
    }

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
        when(postService.findAll()).thenReturn(posts);
        PostController postController = new PostController(postService, cityService);
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
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenAddPost() {
        Post post = new Post(0,
                "Заполните поле",
                "Заполните поле",
                LocalDateTime.now(),
                true, cityService.findById(1));
        PostController postController = new PostController(postService, cityService);
        String page = postController.addPost(model, session);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute("regUser", regUser);
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenFormUpdatePost() {
        Post post = new Post(1,
                "Заполните поле",
                "Заполните поле",
                LocalDateTime.now(),
                true, cityService.findById(1));
        when(postService.findById(1)).thenReturn(post);
        PostController postController = new PostController(postService, cityService);
        String page = postController.formUpdatePost(model, 1, session);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute("regUser", regUser);
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page, is("updatePost"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(1,
                "Заполните поле",
                "Заполните поле",
                LocalDateTime.now(),
                true, new City(1, ""));
        PostController postController = new PostController(postService, cityService);
        String page = postController.updatePost(post);
        verify(postService).update(post);
        Assertions.assertEquals(post.getCity().getName(), "Москва");
        assertThat(page, is("redirect:/posts"));

    }


}