package com.zhenhui.demo.toptop.follow.service;

import java.util.Arrays;
import java.util.Date;

import com.zhenhui.demo.toptop.follow.api.domain.FollowTarget;
import com.zhenhui.demo.toptop.follow.api.domain.TargetType;
import com.zhenhui.demo.toptop.follow.dal.domain.FollowingDO;
import com.zhenhui.demo.toptop.follow.dal.repository.FollowingStorage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@FixMethodOrder
public class StorageTest {

    @Autowired
    private FollowingStorage followingStorage;

    @Test
    public void test() {

        FollowingDO following = new FollowingDO();
        following.setUserId(2);
        following.setTargetType(TargetType.TOPIC);
        following.setTargetId(2000);
        following.setGmtAction(new Date());

        int rows = followingStorage.save(following);
        assertTrue(rows == 1);

        rows = followingStorage.remove(2, new FollowTarget(TargetType.TOPIC, 2000));
        assertTrue(rows == 1);

    }

    @Test
    public void testBatch() {

        FollowingDO following1 = new FollowingDO();
        following1.setUserId(2);
        following1.setTargetType(TargetType.TOPIC);
        following1.setTargetId(2000);
        following1.setGmtAction(new Date());

        FollowingDO following2 = new FollowingDO();
        following2.setUserId(2);
        following2.setTargetType(TargetType.TOPIC);
        following2.setTargetId(2000);
        following2.setGmtAction(new Date());

        int rows = followingStorage.batchSave(Arrays.asList(following1, following2));
        assertEquals(2, rows);


    }
}
