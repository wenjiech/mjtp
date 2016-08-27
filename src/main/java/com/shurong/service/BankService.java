package com.shurong.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 与银行通讯业务类
 * @author Administrator
 *
 */
@Service
public class BankService {
	
	@Value("bank.ccb.host")
	private String CCB_HOST;
	@Value("bank.ccb.port")
	private String CCB_PORT;
	@Value("bank.ccb.custid")
	private String CUST_ID;
	@Value("bank.ccb.userid")
	private String USER_ID;
	@Value("bank.ccb.password")
	private String PASSWORD;
	@Value("bank.ccb.txcode.query")
	private String TXCODE_QUERY;
	
	/**
	 * @param startDate 开始时间
	 * @param endDate	结束时间
	 * @param page		查询页次
	 * @param postStr	定位串（第一次查询为空）
	 * @param accno1	查询帐户号
	 * @return
	 */
	public String generateQueryMessage(String startDate, String endDate, String page, String postStr, String accno1){
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("TX");//创建根结点
		generateMessageHead(rootElement, TXCODE_QUERY);
		generateQueryMessageBody(rootElement, startDate, endDate, page, postStr, accno1);
		return document.asXML();
	}
	
	/**
	 * 
	 * @param rootElement
	 * @param txCode 交易码
	 */
	private void generateMessageHead(Element rootElement, String txCode){
		//请求序列号
		Element snEL = rootElement.addElement("REQUEST_SN");
		snEL.setText("");//TODO 生成流水号
		//客户号
		Element custEL = rootElement.addElement("CUST_ID");
		custEL.setText(CUST_ID);
		//操作员号
		Element userEL = rootElement.addElement("USER_ID");
		userEL.setText(USER_ID);
		//密码
		Element passEL = rootElement.addElement("PASSWORD");
		passEL.setText(PASSWORD);
		//交易码
		Element txEL = rootElement.addElement("TX_CODE");
		txEL.setText(txCode);
		//语言
		Element lanEL = rootElement.addElement("LANGUAGE");
		lanEL.setText("CN");
	}
	
	/**
	 * 
	 * @param rootElement
	 * @param year
	 * @param month
	 * @param page
	 * @param postStr
	 */
	private void generateQueryMessageBody(Element rootElement, String startDate, String endDate, String page, String postStr, String accno1){
		Element bodyEl = rootElement.addElement("TX_INFO");//报文体根节点
		Element accEl = bodyEl.addElement("ACCNO1");//帐号
		accEl.setText(accno1);//TODO 帐户数据从表里面查询，新增帐户维护表
		Element startEl = bodyEl.addElement("STARTDATE");//开始时间
		startEl.setText(startDate);
		Element endEl = bodyEl.addElement("ENDDATE");//结束时间
		endEl.setText(endDate);
		bodyEl.addElement("BARGAIN_FLAG");//交易方向
		bodyEl.addElement("CHECK_ACC_NO");//对方账户
		bodyEl.addElement("CHECK_ACC_NAME");//对方帐户名称
		bodyEl.addElement("REMARK");//摘要
		bodyEl.addElement("LOW_AMT");//最小金额
		bodyEl.addElement("HIGH_AMT");//最大金额
		Element pageEl = bodyEl.addElement("PAGE");//起始页
		pageEl.setText(page);
		Element postStrEl = bodyEl.addElement("POSTSTR");//定位串
		postStrEl.setText(postStr);
		Element totalRecordEl = bodyEl.addElement("TOTAL_RECORD");//每页记录数
		totalRecordEl.setText("200");//最大值200
		bodyEl.addElement("DET_NO");//活存帐户明细号串
	}
}
