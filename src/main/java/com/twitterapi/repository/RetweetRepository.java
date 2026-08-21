package com.twitterapi.repository;

import com.twitterapi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
}
