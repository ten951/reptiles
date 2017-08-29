package com.wyt.reptiles.reptiles.webmagic;

import com.wyt.reptiles.reptiles.dao.DepartmentStatusRepository;
import com.wyt.reptiles.reptiles.entity.DepartmentStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Darcy
 * Created By Darcy on 2017/8/28 下午5:40
 */
@Component
public class KkchePipeline implements Pipeline {
    @Autowired
    private DepartmentStatusRepository repository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, List<String>> prod = (Map<String, List<String>>) resultItems.get("prod");
        for (Map.Entry<String, List<String>> entry : prod.entrySet()) {
            String kkcKey = initKey(entry.getKey(), true);
            String[] split = kkcKey.split(",");
            List<String> value = entry.getValue();
            List<DepartmentStatus> prod1 = value.stream()
                    .map(s -> initStatus(split[0], split[1], s, "prod"))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            repository.save(prod1);

        }
        Map<String, List<String>> dev = (Map<String, List<String>>) resultItems.get("dev");
        for (Map.Entry<String, List<String>> entry : dev.entrySet()) {
            List<DepartmentStatus> dev1 = str2DepartmentStatus(entry, "dev", false);
            repository.save(dev1);
        }
        Map<String, List<String>> qa = (Map<String, List<String>>) resultItems.get("qa");

        for (Map.Entry<String, List<String>> entry : qa.entrySet()) {
            List<DepartmentStatus> dev1 = str2DepartmentStatus(entry, "qa", false);
            repository.save(dev1);
        }
        Map<String, List<String>> devB = (Map<String, List<String>>) resultItems.get("devB");
        for (Map.Entry<String, List<String>> entry : devB.entrySet()) {
            List<DepartmentStatus> dev1 = str2DepartmentStatus(entry, "devB", false);
            repository.save(dev1);
        }
        Map<String, List<String>> qaB = (Map<String, List<String>>) resultItems.get("qaB");
        for (Map.Entry<String, List<String>> entry : qaB.entrySet()) {
            List<DepartmentStatus> dev1 = str2DepartmentStatus(entry, "qaB", false);
            repository.save(dev1);
        }
        Map<String, List<String>> kkc = (Map<String, List<String>>) resultItems.get("kkc");
        for (Map.Entry<String, List<String>> entry : kkc.entrySet()) {
            String kkcKey = initKey(entry.getKey(), true);
            String[] split = kkcKey.split(",");
            List<String> value = entry.getValue();
            List<DepartmentStatus> prod1 = value.stream()
                    .map(s -> initStatus(split[0], split[1], s, "kkc"))
                    .flatMap(List::stream)
                    .filter(s -> {
                        List<DepartmentStatus> dev2 = repository.findByProjectNameAndEnvironment(s.getProjectName(), "kkc");
                        return dev2.isEmpty();
                    })
                    .collect(Collectors.toList());
            repository.save(prod1);

        }
    }

    private List<DepartmentStatus> str2DepartmentStatus(Map.Entry<String, List<String>> entry, String environment, boolean flag) {
        List<String> value = entry.getValue();
        return value.stream()
                .filter(s -> {
                    List<DepartmentStatus> dev2 = repository.findByProjectNameAndEnvironment(s, environment);
                    return dev2.isEmpty();
                })
                .map(s -> initStatus(initKey(entry.getKey(), flag), s, environment)).collect(Collectors.toList());
    }


    private String initKey(String key, boolean flag) {
        String keyPro = key.replaceAll("\\d+", "");
        if (flag) {
            final String key2;
            final String key3;
            if (key.contains("1")) {
                key2 = keyPro + "1";
                key3 = keyPro + "2";
            } else if (key.contains("2")) {
                key2 = keyPro + "3";
                key3 = keyPro + "4";
            } else if (key.contains("3")) {
                key2 = keyPro + "5";
                key3 = keyPro + "6";
            } else if (key.contains("4")) {
                key2 = keyPro + "7";
                key3 = keyPro + "8";
            } else if (key.contains("5")) {
                key2 = keyPro + "9";
                key3 = keyPro + "10";
            } else {
                key2 = null;
                key3 = null;
            }
            if (StringUtils.isNotEmpty(key2) && StringUtils.isNotEmpty(key3)) {
                return key2 + "," + key3;
            } else {
                return "";
            }
        } else {
            return key;
        }

    }


    private DepartmentStatus initStatus(String node, String projectName, String environment) {
        DepartmentStatus status = new DepartmentStatus();
        status.setNode(node);
        status.setReleaseTime(new Date());
        status.setEnvironment(environment);
        status.setProjectName(projectName);
        return status;
    }

    private List<DepartmentStatus> initStatus(String node1, String node2, String projectName, String environment) {
        List<DepartmentStatus> statuses = new ArrayList<>();
        DepartmentStatus status = new DepartmentStatus();
        status.setNode(node1);
        status.setReleaseTime(new Date());
        status.setEnvironment(environment);
        status.setProjectName(projectName);
        statuses.add(status);
        DepartmentStatus status2 = new DepartmentStatus();
        status2.setNode(node2);
        status2.setReleaseTime(new Date());
        status2.setEnvironment(environment);
        status2.setProjectName(projectName);
        statuses.add(status2);
        return statuses;
    }
}
