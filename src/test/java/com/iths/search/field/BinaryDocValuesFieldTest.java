package com.iths.search.field;

import com.iths.search.Indexer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * @author sen.huang
 * @date 2019/2/11.
 */
public class BinaryDocValuesFieldTest {

    Indexer indexer ;
    IndexSearcher is;

    @Before
    public void init() throws Exception{
        indexer = new Indexer("indexDir");
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("indexDir"));
        //通过dir得到的路径下的所有的文件
        IndexReader reader = DirectoryReader.open(fsDirectory);
        //建立索引查询器
        is = new IndexSearcher(reader);

    }

    @Test
    public void testBinaryDocValuesField(){
    }
}
