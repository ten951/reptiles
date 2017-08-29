package com.wyt.reptiles.reptiles.service;


import com.wyt.reptiles.reptiles.entity.DepartmentStatus;

import java.util.List;

/**
 * 发布状态业务接口
 *
 * @author Darcyß
 */
public interface IDepartmentStatusService {
    /**
     * 添加或者更新发布状态(更具项目名称)
     *
     * @param environment 环境
     * @param projectName 项目名称
     * @param node        节点
     * @param ip          IP地址
     */
    DepartmentStatus saveOrUpdate(String environment, String projectName, String node, String ip);

    /**
     * 通过环境名和服务名查询发布信息
     *
     * @param envir       环境名
     * @param projectName 服务名
     */
    List<DepartmentStatus> findByEnvirAndProjectNameLike(String envir, String projectName);
}
