package com.iths.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author sen.huang
 * @date 2019/1/23.
 */
public class Indexer {

    /**
     * 写索引实例
     */
    private IndexWriter writer;

    /**
     *
     * @param indexDir 索引存储目录
     */
    public Indexer(String indexDir) throws IOException {
        //索引所在目录
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        //标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        //保存用于创建 IndexWriter 的所有配置。
        IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
        //实例化 IndexWriter
        writer = new IndexWriter(directory, iwConfig);
    }

    /**
     * 索引目录
     * @param dataDir
     * @return
     * @throws Exception
     */
    public int index(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            //索引指定文件
            indexFile(file);
        }
        //返回索引了多少个文件
        return writer.numDocs();
    }


    /**
     * 写索引
     * @param file
     */
    public void indexFile(File file) throws IOException {
        //输出索引文件的路径
        System.out.println("索引文件：" + file.getAbsolutePath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    /**
     * 获取文档对象
     * @param file
     * @return
     * @throws IOException
     */
    private Document getDocument(File file) throws IOException{
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(file)));
        //Field.Store.YES：把文件内容存索引文件里，为NO就说明不需要加到索引文件里去
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        //文件路径
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }




    /**
     * 关闭写索引
     * @throws IOException
     */
    public void close() throws IOException {
        writer.close();
    }


}
