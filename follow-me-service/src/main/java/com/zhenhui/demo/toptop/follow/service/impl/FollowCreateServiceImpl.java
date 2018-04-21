package com.zhenhui.demo.toptop.follow.service.impl;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import com.zhenhui.demo.toptop.follow.api.service.FollowCreateService;
import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;
import com.zhenhui.demo.toptop.follow.dal.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowCreateServiceImpl implements FollowCreateService {

    @Autowired
    private FollowRepository repository;

    @Override
    public boolean createFollow(long userId, TargetType targetType, long targetId, Date gmtAction) {

        FollowingDO following = new FollowingDO();
        following.setUserId(userId);
        following.setTargetType(targetType);
        following.setTargetId(targetId);
        following.setGmtAction(gmtAction);

        try {
            repository.createFollow(following);
            return true;
        } catch (Exception e) {
            //
        }

        return false;
    }

    @Override
    public boolean createFollows(long userId, TargetType targetType, List<Long> targetIds, Date gmtAction) {

        try {
            repository.createFollows(userId, targetType, targetIds, gmtAction);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeFollow(long userId, TargetType targetType, long targetId) {
        try {
            repository.removeFollow(userId, targetType, targetId);
            return true;
        } catch (Exception e) {
            //
        }

        return false;
    }
}

