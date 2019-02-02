package com.iths.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
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
public class LuceneWeighting {
    private Directory dir; //存放索引的位置

    private String dirStr = "indexDir";

    //准备一下数据，四个人写了四篇文章，Json是boss
    private String ids[]={"1","2","3","4"};
    private String authors[]={"Jack","Marry","John","Json"};
    private String positions[]={"accounting","technician","salesperson","boss"};
    private String titles[]={"Java is a good language.","Java is a cross platform language","Java powerful","You should learn java"};
    private String contents[]={
            "If possible, use the same JRE major version at both index and search time.",
            "When upgrading to a different JRE major version, consider re-indexing. ",
            "Different JRE major versions may implement different versions of Unicode.",
            "For example: with Java 1.4, `LetterTokenizer` will split around the character U+02C6."
    };

    private IndexWriter getIndexWriter() throws IOException {
        dir = FSDirectory.open(Paths.get(dirStr));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir,config);
    }

    @Test
    public void testWrite() throws IOException {
        IndexWriter writer = getIndexWriter();
        for(int i=0;i<ids.length;i++){
            Document document = new Document();
            document.add(new StringField("id",ids[i],Field.Store.YES));
            document.add(new StringField("author",authors[i],Field.Store.YES));
            document.add(new StringField("position",positions[i],Field.Store.YES));
            document.add(new StringField("title",titles[i],Field.Store.YES));
            document.add(new StringField("content",contents[i],Field.Store.NO));
            writer.addDocument(document);
            System.out.println(document.get("title"));
        }
//        writer.commit();
        writer.close();
    }




    @Test
    public void testSearch() throws IOException, ParseException {
        dir = FSDirectory.open(Paths.get(dirStr));
        DirectoryReader reader = DirectoryReader.open(dir);

        System.out.println("实际文档数据["+reader.numDocs()+"]");

        IndexSearcher searcher = new IndexSearcher(reader);

        //实例化分析器
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse("java");

//        String searchField = "title"; //要查询的Field
//        String q = "java"; //要查询的字符串
//        Term term = new Term(searchField, q);
//        Query query = new TermQuery(term);


        TopDocs topDocs = searcher.search(query, 10);

        for(ScoreDoc docNum:topDocs.scoreDocs){
            Document document = searcher.doc(docNum.doc);
            System.out.println("document.author["+document.get("author")+"],position["+document.get("position")+"]");
        }
    }


}
