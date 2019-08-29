package cn.aj.service.impl;

import cn.aj.dao.ExampleDao;
import cn.aj.domain.entity.Example;
import cn.aj.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by itw_yangwj on 2018/7/4.
 */
@Service
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    private ExampleDao exampleDao;

    @Override
    public void insertSelective(Example example) {
        exampleDao.insertSelective(example);
    }

    @Override
    public void delete(Example example) {
        exampleDao.delete(example);
    }

    @Override
    public void update(Example example) {
        exampleDao.update(example);
    }

    @Override
    public Example find(Example example) {
        return exampleDao.find(example);
    }

    @Override
    public List<Example> findAll() {
        return exampleDao.findAll();
    }

    @Override
    public Integer getCount(Example example) {
        return exampleDao.getCount(example);
    }

    @Override
    public void testDubboProvider() {
        System.out.println("------ TestDubboProvider ------");
    }

}
