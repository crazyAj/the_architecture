package cn.aj.dao;

import cn.aj.domain.entity.Example;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by itw_yangwj on 2018/7/4.
 */
@Repository
public interface ExampleDao {

    void insertSelective(Example example);

    void delete(Example example);

    void update(Example example);

    Example find(Example example);

    List<Example> findAll();

    Integer getCount(Example example);

}
