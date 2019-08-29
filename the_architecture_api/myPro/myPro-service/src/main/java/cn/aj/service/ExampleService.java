package cn.aj.service;

import cn.aj.domain.entity.Example;

import java.util.List;

/**
 * Created by itw_yangwj on 2018/7/4.
 */
public interface ExampleService {

    void insertSelective(Example example);

    void delete(Example example);

    void update(Example example);

    Example find(Example example);

    List<Example> findAll();

    Integer getCount(Example example);

    void testDubboProvider();

}
