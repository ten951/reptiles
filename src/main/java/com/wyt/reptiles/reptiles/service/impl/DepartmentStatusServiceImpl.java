package com.wyt.reptiles.reptiles.service.impl;

import com.wyt.reptiles.reptiles.dao.DepartmentStatusRepository;
import com.wyt.reptiles.reptiles.entity.DepartmentStatus;
import com.wyt.reptiles.reptiles.service.IDepartmentStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author Darcy
 * Created By Darcy on 2017/8/24 下午4:19
 */
@Component
public class DepartmentStatusServiceImpl implements IDepartmentStatusService {
    @Autowired
    private DepartmentStatusRepository departmentStatusRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public DepartmentStatus saveOrUpdate(String environment, String projectName, String node, String ip) {
        Date parse = new Date();
        DepartmentStatus status = new DepartmentStatus();
        List<DepartmentStatus> departmentStatusList = departmentStatusRepository.findByProjectNameAndEnvironment(projectName, environment);
        if (null != departmentStatusList && departmentStatusList.size() != 0) {
            status = departmentStatusList.get(0);
            departmentStatusRepository.updateNode(node, projectName, environment, ip);
            status.setNode(node);
            status.setIp(ip);
            return status;
        } else {
            status.setEnvironment(environment);
            status.setProjectName(projectName);
            status.setReleaseTime(parse);
            status.setNode(node);
            status.setIp(ip);
            departmentStatusRepository.save(status);
        }
        return status;
    }

    @Override
    public List<DepartmentStatus> findByEnvirAndProjectNameLike(String envir, String projectName) {
        return departmentStatusRepository.findAll(
                Specifications.where(envirEq(envir)).and(projectNameLike(projectName)),
                new Sort(Sort.Direction.DESC, "releaseTime"));
    }

    private static Specification<DepartmentStatus> envirEq(String envir) {
        return StringUtils.isEmpty(envir) ? null : (entity, query, build) -> build.equal(entity.get("environment").as(String.class), envir);
    }

    private static Specification<DepartmentStatus> projectNameLike(String projectName) {
        return StringUtils.isEmpty(projectName) ? null : (entity, query, build) -> build.like(entity.get("projectName").as(String.class), "%" + projectName + "%");
    }
}
