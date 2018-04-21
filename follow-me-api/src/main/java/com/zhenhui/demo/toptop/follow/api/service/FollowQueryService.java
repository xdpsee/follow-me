package com.zhenhui.demo.toptop.follow.api.service;

import java.util.Date;
import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.Follower;
import com.zhenhui.demo.toptop.follow.api.domain.Following;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;

public interface FollowQueryService {

    int countFollowings(long userId);

    int countFollowers(TargetType targetType, long targetId);

    List<Following> queryFollowings(long userId, Date gmtOffset, boolean ascending, int limit);

    List<Follower> queryFollowers(TargetType targetType, long targetId, Date gmtOffset, boolean ascending, int limit);

}
