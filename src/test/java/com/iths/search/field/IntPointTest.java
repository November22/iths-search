package com.iths.search.field;

import com.iths.search.Indexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.PackedInts;
import org.junit.Before;
import org.junit.Test;
import sun.jvm.hotspot.oops.IntField;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author sen.huang
 * @date 2019/2/11.
 */
public class IntPointTest {
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

    /**
     * 一维数据范围查询
     * @throws IOException
     */
    @Test
    public void testIntFieldIndex1() throws IOException {
        Document document = new Document();
        document.add(new IntPoint("intValue",189));
        document.add(new StoredField("intValue",189));

        Document document2 = new Document();
        document2.add(new IntPoint("intValue",177));
        document2.add(new StoredField("intValue",177));

        Document document3 = new Document();
        document3.add(new IntPoint("intValue",22));
        document3.add(new StoredField("intValue",22));
        indexer.write(document);
        indexer.write(document2);
        indexer.write(document3);
        indexer.commitAndClose();
        //一维情况下的范围查询
        Query query = IntPoint.newRangeQuery("intValue",10,200);
        TopDocs search = is.search(query, 10);

        for(ScoreDoc scoreDoc:search.scoreDocs){
            Document d = is.doc(scoreDoc.doc);
            System.out.println(d.get("intValue"));
        }
    }

    /**
     * 多维数据查询
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testIntFieldIndex2() throws IOException, InterruptedException {
        Document document = new Document();
        document.add(new IntPoint("intValue",189,123,124));
        document.add(new StoredField("intValue","189,123,124"));

        indexer.write(document);
        Document document2 = new Document();
        document2.add(new IntPoint("intValue",191,123,124));
        document2.add(new StoredField("intValue","191,123,124"));
        indexer.write(document2);
        //StoredField只存储不索引，可以和只索引不存储的Field配合使用
        //写入索引是异步化的操作
        indexer.commitAndClose();
        Thread.sleep(1000L);
        //多维查询维情况下的范围查询，维度需要和原本索引的维度一致
        int[] lowerValue = {177,110,120};
        int[] upperValue = {192,123,125};
        Query query = IntPoint.newRangeQuery("intValue",lowerValue,upperValue);
        TopDocs search = is.search(query, 10);

        for(ScoreDoc scoreDoc:search.scoreDocs){
            Document d = is.doc(scoreDoc.doc);
            System.out.println(d.get("intValue"));
        }
    }

    /**
     * 构造集合查询,对应一维数据,即准确索引查询
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testIndexNewSetQuery() throws IOException, InterruptedException{
        Document document = new Document();
        document.add(new IntPoint("intValue",124));
        //索引用于排序、聚合,有:NumericDocValuesField,FloatDocValuesField,DoubleDocValuesField
        //对于多维数据，可以依据业务，指定*DocValuesField的value值
        document.add(new NumericDocValuesField("intValue",124));
        //StoredField只存储不索引，可以和只索引不存储的Field配合使用
        document.add(new StoredField("intValue","124"));

        indexer.write(document);
        Document document2 = new Document();
        document2.add(new IntPoint("intValue",191));
        document2.add(new NumericDocValuesField("intValue",191));
        document2.add(new StoredField("intValue","191"));
        indexer.write(document2);

        //写入索引是异步化的操作
        indexer.commitAndClose();
        Thread.sleep(4000L);
        //多维查询维情况下的范围查询，维度需要和原本索引的维度一致
        Query query = IntPoint.newSetQuery("intValue",123,124,125,191);
        //排序
        SortField[] sortField = new SortField[1];
        sortField[0] = new SortField("intValue",SortField.Type.INT,false);
        Sort sort = new Sort(sortField);
        TopDocs search = is.search(query, 10, sort);

        for(ScoreDoc scoreDoc:search.scoreDocs){
            Document d = is.doc(scoreDoc.doc);
            System.out.println(d.get("intValue"));
        }
    }

    //Query org.apache.lucene.document.IntPoint.newExactQuery(String field, int value)
    //构造精确查询。内部调用的还是newRangeQuery(String field, int lowerValue, int upperValue)。

}
