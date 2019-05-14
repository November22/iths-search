package com.iths.search.cloud;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

public class CloudTest {

	/**
	 * 向cluster插入数据
	 * @throws SolrServerException
	 * @throws IOException
	 */
	@Test
	public void testAddDocument() throws SolrServerException, IOException {
		 CloudSolrClient cloudSolrClient = new  CloudSolrClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184");
		 cloudSolrClient.setDefaultCollection("rz_record");
		 SolrInputDocument document = new SolrInputDocument();
		 document.addField("id", UUID.randomUUID().toString());
		 document.addField("order_id", UUID.randomUUID().toString());
		 document.addField("rz_name", "sen.huang@yeepay.com");
		 document.addField("rz_card_num", "33XXX333");
		 document.addField("rz_time", new Date());
		 
		 cloudSolrClient.add("rz_record",document);
		 cloudSolrClient.commit();
		 
	}
	
	/**
	 * 查询
	 * @throws SolrServerException
	 * @throws IOException
	 */
	@Test
	public void testSearchDocument() throws SolrServerException, IOException {
		CloudSolrClient cloudSolrClient = new  CloudSolrClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184");
		SolrQuery params = new SolrQuery();
		params.set("q", "rz_name:sen.huang");
		params.set("qt", "/select");        
		params.set("collection", "rz_record");
		QueryResponse queryResponse = cloudSolrClient.query( params);
		SolrDocumentList list = queryResponse.getResults();
		System.out.println(list);
	}


}
