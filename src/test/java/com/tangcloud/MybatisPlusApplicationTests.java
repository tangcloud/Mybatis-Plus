package com.tangcloud;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangcloud.mapper.UserMapper;
import com.tangcloud.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class MybatisPlusApplicationTests {

    //继承了mybatis-plus 中的baseMapper类,所有的方法都来着自己的父类,也可以自己编写自己的方法
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        //查询全部数据
        //参数是一个 Wrapper(条件构造器) 没有参数用null
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void insert(){
        User user = new User();
        user.setName("tang");
        user.setAge(14);
        user.setEmail("123@qq.com");
        int insert = userMapper.insert(user); //插入时会自动生成id
        System.out.println(insert);
        System.out.println(user);
    }

    @Test
    void update(){
        User user = new User();
        //通过条件自动拼接动态sql
        user.setId(6L);
        user.setName("test");
        int count = userMapper.updateById(user);
        System.out.println(count);

    }


    //测试乐观锁
    @Test
    void testVersion(){
        //1.查询用户
        User user = userMapper.selectById(1L);
        //2. 修改用户信息
        user.setName("TestVersion");
        user.setEmail("TestVersion@qq.com");

        //模拟另外一个线程执行插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("插队操作");
        user2.setEmail("TestVersion@qq.com");
        userMapper.updateById(user2);

        //由于被插队 乐观锁的愿意 无法修改值
        int i = userMapper.updateById(user);
        if(i == 0){
            System.out.println("被插队,修改失败!!!");
        }
    }

    /**
     * 测试查询
     */
    @Test
    void testSelect(){
        //通过用户id查询
        log.info("通过用户id查询");
        User user = userMapper.selectById(1L);
        System.out.println(user);

        log.info("通过多个id查询用户");
        //通过多个id查询用户 批量查询 (where in(key,key1,key2))
        List<Integer> userIdList = Arrays.asList(1, 2, 3);
        List<User> users = userMapper.selectBatchIds(userIdList);
        users.forEach(System.out::println);

        log.info("自定义查询条件按照版本号查询");
        //条件查询
        Map<String,Object> map = new HashMap<>();
        //自定义查询条件 按照版本号查询
        map.put("version",2);
        List<User> users2 = userMapper.selectByMap(map);
        users2.forEach(System.out::println);

    }

    /**
     * 测试分页查询
     */
    @Test
    void testPage(){
        //参数一: 当前页
        //参数二: 页面数据个数
        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page,null);
        List<User> records = page.getRecords();
        records.forEach(System.out::println);
        System.out.println(page.getTotal());//获取总数
    }

    /**
     * 测试删除
     */
    @Test
    void testDelete(){
        //通过id 删除
        userMapper.deleteById(1L);

        //批量删除
        List<Integer> userIdList = Arrays.asList(1, 2, 3);
        userMapper.deleteBatchIds(userIdList);

        //条件删除
        Map<String,Object> map = new HashMap<>();
        map.put("name","插队操作");
        userMapper.deleteByMap(map);

    }

    /**
     * 测试逻辑删除  逻辑删除后查询时会自动排除逻辑删除的数据
     */
    @Test
    void testDelete2(){
        userMapper.deleteById(1L);
    }



}
