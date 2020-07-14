package com.tangcloud;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangcloud.mapper.UserMapper;
import com.tangcloud.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class MybatisWrapper {

    /**
     * Wrapper 条件构造器
     */

    @Autowired
    private UserMapper userMapper;

    QueryWrapper<User> wrapper = new QueryWrapper<>();
    @Test
    void contextLoads(){
        //查询name不为空 email不为空 年龄大于16岁的数据

        wrapper.isNotNull("name")
                .isNotNull("email")
                .ge("age",16);  //大于
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void testUserName(){
        //根据name 查询
        wrapper.eq("name","test");
        userMapper.selectOne(wrapper);// 查询一个值,出现多个结果使用list 获取 Map
    }

    @Test
    void test3(){
        //查询年龄在20-30岁之间的数据
        wrapper.between("age",20,30);  //区间
        List<User> user = userMapper.selectList(wrapper);//查询结果数
        Integer count = userMapper.selectCount(wrapper);//查询结果数
        System.out.println(count);
    }

    /**
     * 模糊查询
     */
    @Test
    void test4(){
        wrapper.notLike("name","e")     //===> not like
               .likeRight("email","t"); //===> like 't%'
        List<Map<String,Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    /**
     * 连接查询
     */
    @Test
    void test5(){
        //id 在子查询中查询出来
        wrapper.inSql("id","select id from user where id < 3");
        List<Object> objects = userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);
    }

    /**
     * 排序
     */
    @Test
    void test6(){
        wrapper.orderByDesc("id");
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //######################## 更多操作结合官网文档 https://mp.baomidou.com/  https://mybatis.plus


}
