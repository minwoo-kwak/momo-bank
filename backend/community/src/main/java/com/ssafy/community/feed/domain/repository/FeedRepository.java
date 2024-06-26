package com.ssafy.community.feed.domain.repository;

import com.ssafy.community.feed.domain.Comments;
import com.ssafy.community.feed.domain.Feed;
import com.ssafy.community.feed.domain.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Integer> {
//    @Query("SELECT f FROM Feed f WHERE f.isDeleted = false")
//    Page<Feed> findAll(Pageable pageable);

    @Query("SELECT f FROM Feed f WHERE f.groupMember.groupInfo.groupInfoId = :groupId AND f.isDeleted = false")
    Page<Feed> findByGroupId(@Param("groupId") Integer groupId, Pageable pageable);

    Optional<Feed> findByFeedId(Integer feedId);

    void deleteById(Integer feedId);

    @Query("SELECT c FROM Comments c WHERE c.commentId = :commentId")
    Optional<Comments> findCommentById(@Param("commentId") Integer commentId);

    List<Feed> findByGroupMember(GroupMember groupMember);

    // 레포트용. 근 한달간 인기 게시물 세개 읽어오기.
    @Query("SELECT f FROM Feed f WHERE f.createdAt > :cutoff AND f.isDeleted = false ORDER BY f.likesCount DESC")
    Page<Feed> findTopLikedFeedsLastMonth(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

}
