package cn.aj.service.impl;

import cn.aj.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by itw_yangwj on 2018/8/30.
 */
@Slf4j
@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public void testProvider() {
        log.info("-------- myPro-provider - TestServiceImpl ---------");
    }
}
