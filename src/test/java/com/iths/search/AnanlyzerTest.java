package com.iths.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.Iterator;

/**
 * 要看分析器的分析效果，只需要看TokenStream中的内容就可以了。
 * 每个分析器都有一个方法tokenStream，返回的是一个TokenStream对象。
 * @author sen.huang
 * @date 2019/2/12.
 */
public class AnanlyzerTest {

    @Test
    public void testStandardAnanlyzer() throws IOException {
        // 1、创建一个分析器对象
        Analyzer analyzer = new StandardAnalyzer(); // 官方推荐的标准分析器
        // 2、从分析器对象中获得tokenStream对象
        // 参数1：域的名称，可以为null，或者是""
        // 参数2：要分析的文本
        TokenStream tokenStream = analyzer.tokenStream("", "org.apache.lucene. analysis.cc Analyzer");
        // 3、设置一个引用(相当于指针)，这个引用可以是多种类型，可以是关键词的引用，偏移量的引用等等
        // charTermAttribute 对象代表当前的关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 偏移量(其实就是关键词在文档中出现的位置，拿到这个位置有什么用呢？因为我们将来可能要对该关键词进行高亮显示，进行高亮显示要知道这个关键词在哪？)
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);

        KeywordAttribute keywordAttribute = tokenStream.addAttribute(KeywordAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
        // 4、调用tokenStream的reset方法，不调用该方法，会抛出一个异常
        tokenStream.reset();

        // 5、使用while循环来遍历单词列表
        while (tokenStream.incrementToken()) {
            System.out.println("start→" + offsetAttribute.startOffset()); // 关键词起始位置
            // 6、打印单词
            System.out.println(charTermAttribute);
            System.out.println("end→" + offsetAttribute.endOffset()); // 关键词结束位置
            System.out.println("keywordAttribute->"+keywordAttribute.isKeyword());
            System.out.println("typeAttribute->"+typeAttribute.type());
        }
        // 7、关闭tokenStream对象
        tokenStream.close();
    }

    /**
     * SmartChineseAnalyzer
     * @throws IOException
     */
    @Test
    public void testSmartcn() throws IOException{

        //添加停用词
        CharArraySet cas = new CharArraySet( 0, true);
        // 自定义停用词
        String[] self_stop_words = { "的", "了", "呢", "，", "0", "：", ",", "是", "流" };
        for (int i = 0; i < self_stop_words.length; i++) {
            cas.add(self_stop_words[i]);
        }
        // 加入系统默认停用词
        Iterator<Object> itor = SmartChineseAnalyzer.getDefaultStopSet().iterator();
        while (itor.hasNext()) {
            cas.add(itor.next());
        }

        //1、创建一个分析器对象
        //如果使用 stopwords.txt 那必须在包下面操作
        Analyzer analyzer = new SmartChineseAnalyzer(false); // 官方推荐的标准分析器

        // 2、从分析器对象中获得tokenStream对象
        // 参数1：域的名称，可以为null，或者是""
        // 参数2：要分析的文本
        TokenStream tokenStream = analyzer.tokenStream("", "偏移量(其实就是关键词高富帅在文档中出现的位置，拿到这个位置有什么用呢？因为我们将来可能要对该关键词进行高亮显示，进行高亮显示要知道这个关键词在哪？)");
        // 3、设置一个引用(相当于指针)，这个引用可以是多种类型，可以是关键词的引用，偏移量的引用等等
        // charTermAttribute 对象代表当前的关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 偏移量(其实就是关键词在文档中出现的位置，我们将来可能要对该关键词进行高亮显示，进行高亮显示要知道这个关键词在哪)
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);

        KeywordAttribute keywordAttribute = tokenStream.addAttribute(KeywordAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
        // 4、调用tokenStream的reset方法，不调用该方法，会抛出一个异常
        tokenStream.reset();

        // 5、使用while循环来遍历单词列表
        while (tokenStream.incrementToken()) {
            System.out.println("start→" + offsetAttribute.startOffset()); // 关键词起始位置
            // 6、打印单词
            System.out.println(charTermAttribute);
            System.out.println("end→" + offsetAttribute.endOffset()); // 关键词结束位置
        }
        // 7、关闭tokenStream对象
        tokenStream.close();;
    }

    /**
     * 版本问题
     * @throws IOException
     */
    @Test
    public void testIkAnalyzer() throws IOException{
        // 1、创建一个分析器对象
        Analyzer analyzer = new IKAnalyzer(); // 官方推荐的标准分析器
        // 2、从分析器对象中获得tokenStream对象
        // 参数1：域的名称，可以为null，或者是""
        // 参数2：要分析的文本
        TokenStream tokenStream = analyzer.tokenStream("", "偏移量(其实就是关键词高富帅在文档中出现的位置，拿到这个位置有什么用呢？因为我们将来可能要对该关键词进行高亮显示，进行高亮显示要知道这个关键词在哪？)");
        // 3、设置一个引用(相当于指针)，这个引用可以是多种类型，可以是关键词的引用，偏移量的引用等等
        // charTermAttribute 对象代表当前的关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 偏移量(其实就是关键词在文档中出现的位置，拿到这个位置有什么用呢？因为我们将来可能要对该关键词进行高亮显示，进行高亮显示要知道这个关键词在哪？)
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);

        KeywordAttribute keywordAttribute = tokenStream.addAttribute(KeywordAttribute.class);
        TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
        // 4、调用tokenStream的reset方法，不调用该方法，会抛出一个异常
        tokenStream.reset();

        // 5、使用while循环来遍历单词列表
        while (tokenStream.incrementToken()) {
            System.out.println("start→" + offsetAttribute.startOffset()); // 关键词起始位置
            // 6、打印单词
            System.out.println(charTermAttribute);
            System.out.println("end→" + offsetAttribute.endOffset()); // 关键词结束位置
            System.out.println("keywordAttribute->"+keywordAttribute.isKeyword());
            System.out.println("typeAttribute->"+typeAttribute.type());
        }
        // 7、关闭tokenStream对象
        tokenStream.close();;
    }
}
