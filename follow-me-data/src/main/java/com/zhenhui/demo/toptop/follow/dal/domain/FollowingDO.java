package com.zhenhui.demo.toptop.follow.dal.domain;

import java.io.Serializable;
import java.util.Date;

import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("FollowingDO")
@Data
public class FollowingDO implements Serializable {

    private static final long serialVersionUID = -1L;

    private long userId;

    private TargetType targetType;

    private long targetId;

    private Date gmtAction;

    private Integer status = 1;
}





