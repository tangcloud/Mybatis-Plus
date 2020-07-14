package com.tangcloud.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * mybatis-plus id
     * ID_WORKER(默认) 全局唯一id
     * AUTO  id自增
     * UUID  全局唯一id
     * NONE  未设置id
     * INPUT 手动输入
     * ID_WORKER_STR 字符串表示法
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableField(fill = FieldFill.INSERT)  //自动填充创建时间(新增时插入) 需要配合填充策略使用(com\tangcloud\handler\MyMetaObjectHandler.java)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) //自动填充修改时间(新增和修改时插入)
    private Date updateTime;

    @Version  //乐观锁注解 配合乐观锁插件(com\tangcloud\config\MybatisPlusConfig.java)
    private Integer version;


    //application.yml 配置逻辑删除定义的值
    @TableLogic  //配合逻辑删除插件 (com\tangcloud\config\MybatisPlusConfig.java)
    private Integer deleted;  //逻辑删除

}
