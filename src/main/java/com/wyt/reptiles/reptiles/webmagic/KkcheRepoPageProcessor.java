package com.wyt.reptiles.reptiles.webmagic;

import com.wyt.reptiles.reptiles.BCConvert;
import com.wyt.reptiles.reptiles.ICirculate;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Darcy
 * Created By Darcy on 2017/8/28 下午3:09
 */
public class KkcheRepoPageProcessor implements PageProcessor {
    private static String url = "http://confluence.kkche.cn/pages/viewpage.action?pageId=5800060";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
            .setDomain("confluence.kkche.cn")
            .addCookie("JSESSIONID", "B5817AE77095CDB686ED400599908BC8")
            .addCookie("confluence-sidebar.width", "285 ");

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://confluence.kkche.cn/pages/viewpage.action?pageId=5800060)").all());
        Map<String, List<String>> prodMap = initMap("pro-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][1]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("prod", prodMap);
        Map<String, List<String>> devMap = initMap("dev-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 13; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][2]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("dev", devMap);


        Map<String, List<String>> qaMap = initMap("qa-cs", page, 1, 4, (ss, page1, line) -> {
            for (int k = 2; k <= 13; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][3]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("qa", qaMap);

        Map<String, List<String>> devBMap = initMap("devB-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][4]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/p/text()").toString();
                ss.add(s);
            }
        });
        page.putField("devB", devBMap);

        Map<String, List<String>> qaBMap = initMap("qaB-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][5]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/p/text()").toString();
                ss.add(s);
            }
        });
        page.putField("qaB", qaBMap);
        Map<String, List<String>> kkcMap = initMap("kkc-cs", page, 1, 4, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][6]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("kkc", kkcMap);
    }

    @Override
    public Site getSite() {
        return site;
    }


    /**
     * 组装容器(确定list的size)
     *
     * @param keyPrefix key前缀
     * @param page      page
     * @param forVar    循环变量
     * @param forVarMax 循环变量最大值
     * @param circulate 循环接口
     * @return Map
     */
    private Map<String, List<String>> initMap(String keyPrefix, Page page, int forVar, int forVarMax, ICirculate circulate) {
        Map<String, List<String>> hs = new HashMap<>();
        for (int i = forVar; i <= forVarMax; i++) {
            initMap(hs, keyPrefix + i, i, page, circulate);
        }
        return hs;

    }

    /**
     * 组装容器,并对字符串去重,去空格
     *
     * @param hs        数据容器
     * @param key       key
     * @param line      爬虫爬出的列
     * @param page      page
     * @param circulate 循环接口
     * @return Map
     */
    private Map<String, List<String>> initMap(Map<String, List<String>> hs, String key, int line, Page page, ICirculate circulate) {
        if (line == 0) line = 1;
        List<String> ss = new ArrayList<>();
        circulate.circulate(ss, page, line);
        List<String> collect = ss.stream()
                .map(BCConvert::qj2bj)
                .filter(s -> StringUtils.isNotEmpty(s.trim()))
                .distinct()
                .collect(Collectors.toList());
        hs.put(key, collect);
        return hs;
    }

    public static void main(String[] args) {
        Spider.create(new KkcheRepoPageProcessor())
                .addUrl(url)
                .addPipeline(new KkchePipeline())
                .thread(5)
                .run();
    }
}
