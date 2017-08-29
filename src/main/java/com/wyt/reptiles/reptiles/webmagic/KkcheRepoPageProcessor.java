package com.wyt.reptiles.reptiles.webmagic;

import com.wyt.reptiles.reptiles.BCConvert;
import com.wyt.reptiles.reptiles.Test;
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
        //  Document document = Jsoup.parse(page.getHtml().toString());
        page.addTargetRequests(page.getHtml().links().regex("(http://confluence.kkche.cn/pages/viewpage.action?pageId=5800060)").all());
        Map<String, List<String>> prodMap = test("pro-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][1]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("prod", prodMap);
        Map<String, List<String>> devMap = test("dev-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 13; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][2]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("dev", devMap);


        Map<String, List<String>> qaMap = test("qa-cs", page, 1, 4, (ss, page1, line) -> {
            for (int k = 2; k <= 13; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][3]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/text()").toString();
                ss.add(s);
            }
        });
        page.putField("qa", qaMap);

        Map<String, List<String>> devBMap = test("devB-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][4]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/p/text()").toString();
                ss.add(s);
            }
        });
        page.putField("devB", devBMap);

        Map<String, List<String>> qaBMap = test("qaB-cs", page, 1, 5, (ss, page1, line) -> {
            for (int k = 2; k <= 10; k++) {
                String s = page1.getHtml().xpath("//[@class='table-wrap'][5]/table[@class='confluenceTable']/tbody/tr[" + k + "]/td[@class='confluenceTd'][" + line + "]/p/text()").toString();
                ss.add(s);
            }
        });
        page.putField("qaB", qaBMap);
        Map<String, List<String>> kkcMap = test("kkc-cs", page, 1, 4, (ss, page1, line) -> {
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


    private Map<String, List<String>> test(String keyPrefix, Page page, int forVar, int forVarMax, Test test) {
        Map<String, List<String>> hs = new HashMap<>();
        for (int i = forVar; i <= forVarMax; i++) {
            initMap(hs, keyPrefix + i, i, page, test);
        }
        return hs;

    }

    private Map<String, List<String>> initMap(Map<String, List<String>> hs, String key, int line, Page page, Test test) {
        if (line == 0) line = 1;
        List<String> ss = new ArrayList<>();
        test.test(ss, page, line);
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
