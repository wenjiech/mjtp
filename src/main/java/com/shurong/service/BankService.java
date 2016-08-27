package com.shurong.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shurong.exception.BusinessException;
import com.shurong.utils.DateUtils;

/**
 * 与银行通讯业务类
 * @author Administrator
 *
 */
@Service
public class BankService {
	
	private static final Logger logger = LoggerFactory.getLogger(BankService.class);
	
	@Value("bank.ccb.host")
	private String CCB_HOST;
	@Value("bank.ccb.port")
	private int CCB_PORT;
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
	public String generateQueryMessage(String startDate, String endDate, int page, String postStr, String accno1){
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("TX");//创建根结点
		generateMessageHead(rootElement, TXCODE_QUERY);
		generateQueryMessageBody(rootElement, startDate, endDate, page, postStr, accno1);
		String message = document.asXML();
		logger.info("发送请求报文" + message);
		return message;
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
	private void generateQueryMessageBody(Element rootElement, String startDate, String endDate, int page, String postStr, String accno1){
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
		pageEl.setText(String.valueOf(page));
		Element postStrEl = bodyEl.addElement("POSTSTR");//定位串
		postStrEl.setText(postStr);
		Element totalRecordEl = bodyEl.addElement("TOTAL_RECORD");//每页记录数
		totalRecordEl.setText("200");//最大值200
		bodyEl.addElement("DET_NO");//活存帐户明细号串
	}
	
	/**
	 * 银行交易明细查询
	 * @param year
	 * @param month
	 */
	public void doGetBankData(int year, int month){
		logger.info("开始从银行获取数据，获取年份：%s 月份：%s", year, month);
		//TODO 账号列表在数据库维护，从数据库获取
		List<String> accountList = new ArrayList<>();
		for (String accountNo : accountList){
			logger.info("获取账号%s数据，开始", accountNo);
			//TODO 如果该帐户当月已经查询完毕不再发起查询
			
			//生成发送报文
			String message = generateQueryMessage(DateUtils.getFirstDayOfMonth(year, month), DateUtils.getLastDayOfMonth(year, month), 1, "", accountNo);
			//发送报文
			String respMessage = sendMessage(message);
		}
	}
	
    /**发送报文
     * @param message
     * @return 返回的报文
     * @throws Exception
     */
    public String sendMessage(String message) {
		// TODO Auto-generated method stub
		Socket socket=null;
		BufferedReader reader =null;
		PrintWriter writer = null;
		StringBuffer sb =new StringBuffer();
		try 
		{
			socket = new Socket(CCB_HOST, CCB_PORT);
			socket.setSoTimeout(150*1000);////设置超时时间150秒
			// 用得到的会话对象构造输入输出流
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
			writer.println(message);//发送编码后的报文
			writer.flush();////立马发送
			// 通过输入流接收服务器信息
			char[] cbuf=new char[8912];
			
			while(true)
			{
				int intLength=reader.read(cbuf);
				if(intLength==-1)
					break;
				sb.append(new String(cbuf,0,intLength));
			}
			System.out.println("返回报文：" + "\r\n"+sb.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error("查询银行报文时发生网络异常", e);
			throw new BusinessException("1001", "查询银行报文时发生网络异常");
		}
		finally
		{
			try 
			{
				if(reader!=null)
					reader.close();
				if(writer!=null)
					writer.close();
				if(socket!=null)
					socket.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
    
    /**
     * 解析银行返回报文
     * @param respMessage
     */
    public void doResponseMessage(String respMessage){
    	
    }
}
