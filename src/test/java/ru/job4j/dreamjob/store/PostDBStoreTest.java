package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PostDBStoreTest {

    @BeforeEach
    public void prepareDb() {
        try (Connection cn = (new Main().loadPool()).getConnection();
             Statement st = cn.createStatement()) {
            st.execute("DELETE FROM post");
        } catch (Exception e) {
        }
    }

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(100,
                "Java Job",
                "Some Job",
                LocalDateTime.now(),
                true,
                new City(1, "Spb"));
        post.setId(store.add(post).getId());
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        Assertions.assertEquals(postInDb, post);
    }

    @Test
    public void whenCreateTwoPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(100,
                "Java Job",
                "Some Job",
                LocalDateTime.now(),
                true,
                new City(1, "Spb"));
        Post post2 = new Post(200,
                "Python Job",
                "Some Job",
                LocalDateTime.now(),
                true,
                new City(1, "Spb"));
        post1.setId(store.add(post1).getId());
        post2.setId(store.add(post2).getId());
        List<Post> posts = List.of(post1, post2);
        List<Post> postsInDb = store.findAll();
        assertTrue(postsInDb.containsAll(posts));
        assertEquals(2, postsInDb.size());
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(100,
                "Java Job",
                "Some Job",
                LocalDateTime.now(),
                true,
                new City(1, "Spb"));
        post.setId(store.add(post).getId());
        post.setDescription("Changed description");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        Assertions.assertEquals(postInDb, post);
    }


}