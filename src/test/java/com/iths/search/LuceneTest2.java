package com.iths.search;

import org.apache.lucene.store.Directory;

/**
 * @author sen.huang
 * @date 2019/2/2.
 */
public class LuceneTest2 {

    private Directory dir; //存放索引的位置

    //准备一下用来测试的数据
    private String ids[] = {"1", "2", "3"}; //用来标识文档
    private String citys[] = {"shanghai", "nanjing", "qingdao"};
    private String descs[] = {
            "Shanghai is a bustling city.",
            "Nanjing is a city of culture.",
            "Qingdao is a beautiful city"
    };
}
