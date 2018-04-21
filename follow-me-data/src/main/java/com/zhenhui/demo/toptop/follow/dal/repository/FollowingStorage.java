package com.zhenhui.demo.toptop.follow.dal.repository;

import java.util.List;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;
import com.zhenhui.demo.toptop.follow.dal.utils.FollowingInsertsProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FollowingStorage {

    @Insert(
        "INSERT INTO followings (user_id"
            + ", target_type"
            + ", target_id"
            + ", gmt_action"
            + ", status) VALUES (#{userId}"
            + ", #{targetType}"
            + ", #{targetId}"
            + ", #{gmtAction}"
            + ", 1) on duplicate key update status = 1;")
    int save(FollowingDO following);

    @InsertProvider(type = FollowingInsertsProvider.class, method = "insertFollows")
    int batchSave(@Param("followings") List<FollowingDO> followings);

    @Update(
        "update followings set status = 0 "
            + "where user_id = #{userId} "
            + "and target_type = #{target.type} "
            + "and target_id = #{target.targetId}"
    )
    int remove(@Param("userId") long userId, @Param("target") FollowTarget target);

}


