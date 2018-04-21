package com.zhenhui.demo.toptop.follow.service;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.Follower;
import com.zhenhui.demo.toptop.follow.api.domain.Following;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowerCache;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowingCache;
import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;
import com.zhenhui.demo.toptop.follow.dal.repository.FollowRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@FixMethodOrder
public class RepositoryTest {

    @Autowired
    private FollowRepository repository;
    @Autowired
    private FollowerCache followerCache;
    @Autowired
    private FollowingCache followingCache;

    @Test
    public void testFollow() {

        FollowingDO following = new FollowingDO();
        following.setUserId(2);
        following.setTargetType(TargetType.TOPIC);
        following.setTargetId(2000);
        following.setGmtAction(new Date());


        try {
            repository.createFollow(following);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        List<Following> followings = followingCache.query(2, null, true, 10);
        assertEquals(1, followings.size());

        List<Follower> followers = followerCache.query(new FollowTarget(TargetType.TOPIC, 2000), null, true, 10);
        assertEquals(1, followers.size());

    }

    @Test
    public void testUnfollow() {

        repository.removeFollow(2, TargetType.TOPIC, 2000);

        List<Following> followings = followingCache.query(2, null, true, 10);
        assertEquals(0, followings.size());

        List<Follower> followers = followerCache.query(new FollowTarget(TargetType.TOPIC, 2000), null, true, 10);
        assertEquals(0, followers.size());


    }

}
