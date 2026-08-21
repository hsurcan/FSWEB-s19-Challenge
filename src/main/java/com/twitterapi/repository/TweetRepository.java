package com.twitterapi.repository;

import com.twitterapi.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
