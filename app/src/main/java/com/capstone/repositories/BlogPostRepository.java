package com.capstone.repositories;

import com.capstone.models.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM blog_post
            WHERE id NOT IN (
                SELECT id FROM (
                    SELECT id
                    FROM blog_post
                    ORDER BY id DESC
                    LIMIT :maxPosts
                ) kept_posts
            )
            """, nativeQuery = true)
    int deleteOlderPostsKeepingNewest(@Param("maxPosts") int maxPosts);
}
