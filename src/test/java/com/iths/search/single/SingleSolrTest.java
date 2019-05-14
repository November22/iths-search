package com.iths.search.single;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Solr的Field中没有BigDecimal
 * @author sen.huang
 * @date 2019/5/14.
 */
public class SingleSolrTest {

    private SolrClient solrClient;

    @Before
    public void init(){
        solrClient = new HttpSolrClient("http://localhost:8080/solr/fe_product_detail");
    }

    /**
     * 以实体的方式add
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testAdd() throws IOException, SolrServerException {
        FeProductDetail productDetail = new FeProductDetail();
        productDetail.setId(UUID.randomUUID().toString().replace("-",""));
        productDetail.setOrderId(UUID.randomUUID().toString().replace("-",""));
        productDetail.setProductName("山寨苹果");
        productDetail.setProductPrice(new Double("12580"));
        productDetail.setDescription("MacPro 2018 你值得拥有！");
        solrClient.addBean(productDetail);
        solrClient.commit();
    }

    /**
     * 将查询结果封装到实体
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testQuery() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery("text:MacPro"); // 构造搜索条件
        QueryResponse queryResponse = solrClient.query(solrQuery);
        List<FeProductDetail> beans = queryResponse.getBeans(FeProductDetail.class);
        System.out.println(beans);
    }


    /**
     * 高亮查询
     */
    @Test
    public void testHighlighting() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery("text:MacPro"); // 构造搜索条件
        //开启高亮
        solrQuery.setHighlight(true);
        // 高亮字段
        solrQuery.addHighlightField("product_name");
        // 高亮字段
        solrQuery.addHighlightField("description");
        // 标记，高亮关键字前缀
        solrQuery.setHighlightSimplePre("<span style='color:red;'>");
        // 后缀
        solrQuery.setHighlightSimplePost("</span>");

        QueryResponse queryResponse = solrClient.query(solrQuery);
        List<FeProductDetail> beans = queryResponse.getBeans(FeProductDetail.class);

        //将高亮数据回写到数据
        Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
        for(Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()){
            for(FeProductDetail detail:beans){
                if(highlighting.getKey().equals(detail.getId())){
                    if(highlighting.getValue().get("product_name") != null){
                        detail.setProductName(highlighting.getValue().get("product_name").get(0));
                    }
                    if(highlighting.getValue().get("description") != null){
                        detail.setDescription(highlighting.getValue().get("description").get(0));
                    }
                }
            }
        }
        System.out.println(beans);
    }

    @Test
    public void testDelete() throws IOException, SolrServerException {
        solrClient.deleteById("");
    }

}
