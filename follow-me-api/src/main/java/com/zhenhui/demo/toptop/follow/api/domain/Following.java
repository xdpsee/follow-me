package com.zhenhui.demo.toptop.follow.api.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Following implements Serializable {

    private static final long serialVersionUID = -1L;

    private FollowTarget target;

    private long gmtAction;

}




