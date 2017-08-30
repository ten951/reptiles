package com.wyt.reptiles.reptiles.service.impl;

import com.wyt.reptiles.reptiles.Constants;
import com.wyt.reptiles.reptiles.service.IKkcService;
import com.wyt.reptiles.reptiles.webmagic.KkchePipeline;
import com.wyt.reptiles.reptiles.webmagic.KkcheRepoPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

/**
 * @author Darcy
 * Created By Darcy on 2017/8/28 下午6:03
 */
@Service
public class KkcServiceImpl implements IKkcService {
    @Autowired
    private KkchePipeline pipeline;
    @Override
    public void kkcinit() {
        Spider.create(new KkcheRepoPageProcessor())
                .addUrl(Constants.REPTILES_ROOT)
                .addPipeline(pipeline)
                .run();
    }
}
