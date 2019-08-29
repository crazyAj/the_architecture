package cn.aj.domain.entity;

import lombok.Data;

/**
 * Created by itw_yangwj on 2018/7/17.
 */
@Data
public class InvertEvent<T,V> {
    private T t;
    private V v;
}
