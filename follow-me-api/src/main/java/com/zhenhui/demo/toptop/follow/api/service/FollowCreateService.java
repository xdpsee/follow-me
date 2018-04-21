package com.zhenhui.demo.toptop.follow.api.service;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.TargetType;

public interface FollowCreateService {

    boolean createFollow(long userId, TargetType type, long targetId, Date gmtAction);

    boolean createFollows(long userId, TargetType type, List<Long> targetIds, Date gmtAction);

    boolean removeFollow(long userId, TargetType type, long targetId);
}


