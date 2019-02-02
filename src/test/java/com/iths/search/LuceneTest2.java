package com.iths.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author sen.huang
 * @date 2019/2/2.
 */
public class LuceneTest2 {

    private Directory dir; //存放索引的位置

    private String dirStr = "indexDir";

    //准备一下用来测试的数据
    private String ids[] = {"1", "2", "3"}; //用来标识文档
    private String citys[] = {"shanghai", "nanjing", "qingdao"};
    private String descs[] = {
            "Shanghai is a bustling city.",
            "Nanjing is a city of culture.",
            "Qingdao is a beautiful city"
    };

    /**
     * 获取写索引工具
     * @return
     * @throws IOException
     */
    private IndexWriter getIndexWriter() throws IOException {
        dir = FSDirectory.open(Paths.get(dirStr));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir,config);
    }


    @Test
    public void testIndexWrite() throws IOException {
        IndexWriter writer = getIndexWriter();
        for(int i=0;i<ids.length;i++){
            Field id = new StringField("id",ids[i],Field.Store.YES);
            Field city = new StringField("city",citys[i],Field.Store.YES);
            Field desc = new StringField("desc",descs[i],Field.Store.YES);
            Document document = new Document();
            document.add(id);
            document.add(city);
            document.add(desc);
            writer.addDocument(document);
        }
        writer.close();
    }


    @Test
    public void testIndexReader() throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(dirStr));
        DirectoryReader reader = DirectoryReader.open(directory);
        System.out.println("最大文档数["+reader.maxDoc()+"]");
        System.out.println("实际文档数["+reader.numDocs()+"]");
    }

    /**
     * 合并前删除
     */
    @Test
    public void testDeleteBeforeMerge() throws IOException {
        IndexWriter writer = getIndexWriter();
        System.out.println("删除前实际文档["+writer.numDocs()+"]");
        writer.deleteDocuments(new Term("id","1"));
        writer.commit(); //提交删除,并没有真正删除
        System.out.println("删除后实际文档["+writer.numDocs()+"]");
        System.out.println("删除后最大文档["+writer.maxDoc()+"]");
        writer.close();
    }

    @Test
    public void testDeleteAfterMerge() throws IOException {
        IndexWriter writer = getIndexWriter();
        System.out.println("删除前实际文档["+writer.numDocs()+"]");
        writer.deleteDocuments(new Term("id","1"));
        writer.deleteDocuments(new Term("id","1"));
        writer.forceMergeDeletes();////强制合并（强制删除），没有索引了
        writer.commit();
        System.out.println("删除后实际文档["+writer.numDocs()+"]");
        System.out.println("删除后最大文档["+writer.maxDoc()+"]");
        writer.close();
    }

    /**
     * 修改
     * 实质：1.新建一个Docment，2.替换指定的Document
     * @throws IOException
     */
    @Test
    public void testUpdate() throws IOException {
        IndexWriter writer = getIndexWriter();
        Document document  = new Document();
        document.add(new StringField("id", ids[1], Field.Store.YES));
        document.add(new StringField("city", "nanjing-Update", Field.Store.YES));
        document.add(new TextField("descs", "nanjing update", Field.Store.YES));
        writer.updateDocument(new Term("id","2"),document);
        writer.commit();
        writer.close();
    }

    @Test
    public void get() throws IOException, ParseException {
        FSDirectory directory = FSDirectory.open(Paths.get(dirStr));
        DirectoryReader reader = DirectoryReader.open(directory);
        //建立索引查询器
        IndexSearcher searcher = new IndexSearcher(reader);
        //实例化分析器
        Analyzer analyzer = new StandardAnalyzer();

        // 建立查询解析器
        // 第一个参数是要查询的字段； 第二个参数是分析器Analyzer
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse("java");
        TopDocs hits = searcher.search(query, 10);
        for(ScoreDoc doc:hits.scoreDocs){
            Document document = searcher.doc(doc.doc);
            System.out.println(document.get("author"));
            System.out.println(document.get("position"));
//            System.out.println(document.get("descs"));
        }
    }



}
