package com.zhenhui.demo.toptop.follow.service.impl;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.Follower;
import com.zhenhui.demo.toptop.follow.api.domain.Following;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import com.zhenhui.demo.toptop.follow.api.service.FollowQueryService;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowerCache;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowQueryServiceImpl implements FollowQueryService {

    @Autowired
    private FollowerCache followerCache;
    @Autowired
    private FollowingCache followingCache;

    @Override
    public int countFollowings(long userId) {
        return followingCache.countFollowings(userId);
    }

    @Override
    public int countFollowers(TargetType targetType, long targetId) {
        return followerCache.countFollowers(new FollowTarget(targetType, targetId));
    }

    @Override
    public List<Following> queryFollowings(long userId, Date gmtOffset, boolean ascending, int limit) {
        return followingCache.query(userId, gmtOffset, ascending, limit);
    }

    @Override
    public List<Follower> queryFollowers(TargetType targetType, long targetId, Date gmtOffset, boolean ascending,
                                         int limit) {
        return followerCache.query(new FollowTarget(targetType, targetId), gmtOffset, ascending, limit);
    }
}
