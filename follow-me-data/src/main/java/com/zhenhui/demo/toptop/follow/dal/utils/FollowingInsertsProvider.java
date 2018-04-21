package com.zhenhui.demo.toptop.follow.dal.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;

@SuppressWarnings("unused")
public class FollowingInsertsProvider {

    private static final ThreadLocal<DateFormat> dateFormat  = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public String insertFollows(Map<String,Object> params) {

        List<FollowingDO> followings = (List<FollowingDO>)params.get("followings");

        StringBuilder builder = new StringBuilder();
        builder.append("insert into followings (user_id,target_type,target_id,gmt_action,status) values ");
        for (FollowingDO following : followings) {
            builder.append(String.format("(%d,%d,%d,'%s',1),", following.getUserId(), following.getTargetType().code, following.getTargetId(), dateFormat.get().format(following.getGmtAction())));
        }

        builder.setLength(builder.length() - 1);
        builder.append(" on duplicate key update status = 1;");

        return builder.toString();
    }


}
