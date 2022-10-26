package ru.job4j.dreamjob.services;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;
import java.util.Objects;

@ThreadSafe
@Service
public class PostService {
    private final PostStore postStore;
    private final CityService cityService;

    public PostService(PostStore postStore, CityService cityService) {
        this.postStore = postStore;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        return postStore.findAll();
    }

    public void add(Post post) {
        updateCityInPost(post);
        postStore.add(post);
    }

    private void updateCityInPost(Post post) {
        post.setCity(cityService.findOrDefault(post.getCity().getId()));
    }

    public Post findById(int id) {
        return postStore.findById(id);
    }

    public void update(Post post) {
        updateCityInPost(post);
        postStore.update(post);
    }
}
