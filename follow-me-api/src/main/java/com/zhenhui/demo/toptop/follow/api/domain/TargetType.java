package com.zhenhui.demo.toptop.follow.api.domain;

import java.util.Arrays;

public enum TargetType {
    USER(1, "用户"),
    TOPIC(2, "话题"),
    ;

    public final int code;
    public final String comment;

    TargetType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static TargetType valueOf(int code) {
        return Arrays.stream(values())
            .filter(type -> code == type.code)
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }
}



