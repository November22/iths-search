package com.iths.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 根据索引搜索
 * @author sen.huang
 * @date 2019/1/23.
 */
public class Searcher {

    public static void search(String indexDir,String q) throws IOException, ParseException {
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexDir));
        //通过dir得到的路径下的所有的文件
        IndexReader reader = DirectoryReader.open(fsDirectory);
        //建立索引查询器
        IndexSearcher is = new IndexSearcher(reader);
        //实例化分析器
        Analyzer analyzer = new StandardAnalyzer();

        // 建立查询解析器
        // 第一个参数是要查询的字段； 第二个参数是分析器Analyzer
        QueryParser parser = new QueryParser("fullPath", analyzer);
        // 根据传进来的p查找,即查询条件
        Query query = parser.parse(q);
        /**
         * 第一个参数是通过传过来的参数来查找得到的query； 第二个参数是要出查询的行数
         */
        TopDocs hits = is.search(query, 10);
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            System.out.println("scoreDoc.doc["+scoreDoc.doc+"]");
            Document document = is.doc(scoreDoc.doc);
            System.out.println("fullPath["+document.get("fullPath")+"]");
            System.out.println("contents["+document.get("contents")+"]");
        }
        // 关闭reader
        reader.close();
    }
}
