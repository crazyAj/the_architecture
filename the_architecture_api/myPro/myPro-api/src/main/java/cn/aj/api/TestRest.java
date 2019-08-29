package cn.aj.api;

import cn.aj.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by itw_yangwj on 2018/6/20.
 */
@Slf4j
@Controller
@Path("/{version}/test")
public class TestRest extends BaseRest {

    @Autowired
    private TestService testService;

    /**
     *  测试 zookeeper + dubbo
     */
    @POST
    @Path("/testDubbo")
    public String testDubbo() {
        log.info("------- myPro-api [START] -------");
        testService.testProvider();
        log.info("------- myPro-api [END] -------");
        return "SUCCESS";
    }

}
