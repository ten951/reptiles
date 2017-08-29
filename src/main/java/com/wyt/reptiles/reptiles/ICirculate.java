package com.wyt.reptiles.reptiles;

import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * @author Darcy
 * 函数式接口,抽离爬虫Xpath选择器变化部分
 */
@FunctionalInterface
public interface ICirculate {
    void circulate(List<String> ss, Page page, int line);
}
