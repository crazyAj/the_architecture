package cn.aj.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by itw_yangwj on 2018/6/26.
 */
@Data
public class Example implements Serializable{
    private String id;
    private String empKey;
    private String empValue;
}
