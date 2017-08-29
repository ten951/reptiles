package com.wyt.reptiles.reptiles.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 部署状态实体
 *
 * @author Darcy
 * Created By Darcy on 2017/8/24 下午3:57
 */
@Data
@Entity
@Table(name = "t_department_status")
public class DepartmentStatus extends IDEntity {
    //发布的环境
    private String environment;
    //项目名称dao
    private String projectName;
    //发布的节点
    private String node;
    //发布时间∂
    private Date releaseTime;
    //IP地址(IPV4)
    private String ip;

    public DepartmentStatus() {
    }


}
