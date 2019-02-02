package com.iths.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

import java.io.IOException;

/**
 * @author sen.huang
 * @date 2019/1/23.
 */
public class LuceneTest {

    private static final String indexDir = "/Users/iths/Documents/tmpFile/index";

    @Test
    public void testIndex() throws Exception {
        Indexer indexer = new Indexer(indexDir);
        int index = indexer.index("/Users/iths/Documents/tmpFile/compare");
        System.out.println("index["+index+"]");
        indexer.close();
    }

    @Test
    public void testSearch() throws IOException, ParseException {
        Searcher.search(indexDir,"cc-QAd");
    }


}
