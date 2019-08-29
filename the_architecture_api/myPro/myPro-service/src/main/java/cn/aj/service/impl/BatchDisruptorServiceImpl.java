package cn.aj.service.impl;

import cn.aj.service.BatchDisruptorService;
import cn.aj.service.ExampleService;
import cn.aj.commons.constants.Constant;
import cn.aj.commons.utils.fileUtils.FileFunc;
import cn.aj.domain.dto.ExampleDTO;
import cn.aj.domain.entity.Example;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by itw_yangwj on 2018/7/17.
 */
@Slf4j
@Component
public class BatchDisruptorServiceImpl extends BatchDisruptorService<ExampleDTO,Example> {

    private static final int BUFFER_SIZE = Integer.valueOf(FileFunc.getPropValue(Constant.CONFIG_PATH, "buffer_size"));
    private static final int FIRST_PARALLEL_SIZE = Integer.valueOf(FileFunc.getPropValue(Constant.CONFIG_PATH, "first_parallel_size"));
    private static final int SECOND_PARALLEL_SIZE = Integer.valueOf(FileFunc.getPropValue(Constant.CONFIG_PATH, "second_parallel_size"));
    private static final int THIRD_PARALLEL_SIZE = Integer.valueOf(FileFunc.getPropValue(Constant.CONFIG_PATH, "third_parallel_size"));

    @Autowired
    private ExampleService exampleService;

    public BatchDisruptorServiceImpl(){
        super(BUFFER_SIZE, FIRST_PARALLEL_SIZE, SECOND_PARALLEL_SIZE, THIRD_PARALLEL_SIZE);
    }

    @Override
    public ExampleDTO packageData(Example example) {
        log.info("--------- 1 packageData --------- " + example.getId());
        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setData(JSONObject.toJSONString(example));
        //模拟延时
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exampleDTO;
    }

    @Override
    public void recordData(Example example) {
        log.info("--------- 2 recordData --------- " + example.getId());
        exampleService.insertSelective(example);
        //模拟延时
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendData(ExampleDTO exampleDTO) {
        log.info("--------- 3 sendData --------- " + exampleDTO.getData());
        //模拟延时
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
