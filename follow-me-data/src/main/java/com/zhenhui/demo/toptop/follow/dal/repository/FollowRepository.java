package com.zhenhui.demo.toptop.follow.dal.repository;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowerCache;
import com.zhenhui.demo.toptop.follow.dal.cache.FollowingCache;
import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class FollowRepository {

    @Autowired
    private FollowerCache followerCache;
    @Autowired
    private FollowingCache followingCache;
    @Autowired
    private FollowingStorage followingStorage;

    @Transactional(rollbackFor = Exception.class)
    public void createFollow(FollowingDO following) {

        final FollowTarget target = new FollowTarget(following.getTargetType(), following.getTargetId());

        followingCache.save(following.getUserId(), target, following.getGmtAction());
        followerCache.save(target, following.getUserId(), following.getGmtAction());

        FollowingDO follow = new FollowingDO();
        follow.setUserId(following.getUserId());
        follow.setTargetType(target.getType());
        follow.setTargetId(target.getTargetId());
        follow.setGmtAction(following.getGmtAction());

        final boolean success = followingStorage.save(following) > 0;
        if (!success) {
            throw new RuntimeException("save following exception");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createFollows(long userId, TargetType targetType, List<Long> targetIds, Date gmtAction) {
        final List<FollowTarget> targets = targetIds.stream().map(targetId -> new FollowTarget(targetType, targetId)).collect(toList());

        followingCache.save(userId, targets, gmtAction);
        targets.forEach(target -> followerCache.save(target, userId, gmtAction));

        followingStorage.batchSave(targets.stream().map(target -> {
            FollowingDO following = new FollowingDO();
            following.setUserId(userId);
            following.setTargetType(targetType);
            following.setTargetId(target.getTargetId());
            following.setGmtAction(gmtAction);
            return following;
        }).collect(toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeFollow(long userId, TargetType targetType, long targetId) {

        final FollowTarget target = new FollowTarget(targetType, targetId);
        followerCache.remove(target, userId);
        followingCache.remove(userId, target);
        followingStorage.remove(userId, new FollowTarget(targetType, targetId));
    }

}


