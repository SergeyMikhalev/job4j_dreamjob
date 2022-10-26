package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger newId = new AtomicInteger(4);

    public PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Simple job", LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Simple job too", LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Simple job still", LocalDateTime.now()));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        int postId = newId.getAndIncrement();
        post.setId(postId);
        posts.put(postId, post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }
}
