package com.wyt.reptiles.reptiles.dao;

import com.wyt.reptiles.reptiles.entity.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发布状态持久层
 *
 * @author Darcy
 * Created By Darcy on 2017/8/24 下午4:06
 */
@Repository
public interface DepartmentStatusRepository extends BaseRepository<DepartmentStatus, Long>, JpaSpecificationExecutor<DepartmentStatus> {
    /**
     * 通过项目名称模糊查询 按发布时间倒序
     *
     * @param envir       环境名
     * @param projectName 项目名称
     * @return 发布状态列表
     */
    List<DepartmentStatus> findByEnvironmentAndProjectNameLikeOrderByReleaseTimeDesc(String envir, String projectName);

    /**
     * 通过项目名和环境查询列表
     *
     * @param projectName 项目名
     * @param environment 环境
     * @return 列表
     */
    List<DepartmentStatus> findByProjectNameAndEnvironment(String projectName, String environment);

    /**
     * 更新发布节点
     *
     * @param node        发布节点
     * @param projectName 项目名称
     * @param environment 环境
     * @return 影响行数
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE t_department_status ds SET ds.node = ?1,ds.ip=?4 WHERE ds.project_name =?2 AND ds.environment =?3", nativeQuery = true)
    int updateNode(String node, String projectName, String environment, String ip);
}
