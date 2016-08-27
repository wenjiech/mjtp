package com.shurong.demo;
/**
 * @Customer Shanghai creative Catering Management Limited Bank Reconciliation System
 * @Developer Copyright  2015 ShangHai-EFS
 * @Author Anu.Liang
 * @Date 2015-09-28
 */
import javax.imageio.IIOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.xml.transform.TransformerException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.LinkedList;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;   
public class ManJiTianPin extends JFrame
{	 
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField StartDateField;
    private JTextField EndDateField;
    JProgressBar progressbar;
	static String StartDateString=null;
	static String EndDateString=null;
	//public static final String strReqqq="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>20150915901</REQUEST_SN><CUST_ID>SHP773718882#001</CUST_ID><USER_ID>WLPT01</USER_ID><PASSWORD>8pmmkm</PASSWORD><TX_CODE>6W0300</TX_CODE><LANGUAGE>CN</LANGUAGE><TX_INFO><ACC_NO>31001505400050026797</ACC_NO><START_DATE>20150909</START_DATE><END_DATE>20150909</END_DATE><START_PAGE>1</START_PAGE><POSTSTR></POSTSTR><CONDITION1 ></CONDITION1 ><CONDITION2></CONDITION2 ></TX_INFO></TX>";
	static LinkedList<String> list = new LinkedList<String>();
	private static String req_meg_parent=null;
	private static String req_meg_child=null;
	public final  String strHost="127.0.0.1";//外联客户端所安装的机器IP地址
    public final static  String fileppath="D:\\file\\Result.txt";
	public final  int  intPort=8888;//外联初始化设置的端口号
	public static int count = 0;
	public static int recordcount=1;
	public final static String Str1="<ACCNO1>";
	public final String Str3="<CRE_NO>";
	public final String Str2="<TRANDATE>";
	public final String Str4="<MESSAGE>";
	public final String Str5="<AMT>";
	public final String Str6="<AMT1>";
	public final String Str7="<FLAG1>";
	public final String Str8="<ACC_NAME1>";
	public final String Str9="<TRAN_FLOW>";
	public String Str10="<DET>";
	public final static String url = "jdbc:mysql://efs.multiconceptslink.com:3306/BankDataToBackDB";
    public final String name = "com.mysql.jdbc.Driver";  
    public final static String user = "root";  
    public final static String password = "2wsxCde3";
	static BufferedReader reader = null;
	public String strReq=null;
	@SuppressWarnings("unused")
	private String ACC_NO;
	private String CREDIT_NO;
	private String TRAN_DATE;
	private String ABSTRACT;
	private String AMOUNT;
	private String BALANCE;
	private String dORc;
	private String ACC_NAME;
	private String INDIVIDUAL;
	private String ID;
	private String PostStr;
	private String RETURN_MSG;
	private int PageNum=0;
	private boolean NextFlag;
	private boolean Datecomparisonmark;
	private LinkedList<String> ListLine;
	static Connection conn=null;
	static Statement sql;
	public String filename=null;
	Calendar c=Calendar.getInstance();
    int year=c.get(Calendar.YEAR);
    int month=c.get(Calendar.MONTH);
    int date=c.get(Calendar.DATE);
    String Fname="D:/log_file_"+String.valueOf(year)+String.valueOf(month)+String.valueOf(date)+".txt";
    String sqlmutiple="insert into bdtbt(CREDIT_NO,TRAN_DATE,ABSTRACT,BORROW_MONEY,LOAN_MONEY,BALANCE,dORc,ACC_NAME,INDIVIDUAL,ID,ACC_NO,Unique_Index)values(?,?,?,?,?,?,?,?,?,?,?,?)";
    public ManJiTianPin() {
    	Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		filename="D:\\file\\LogFile_"+format.format(date)+".xml";
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 370, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.GRAY); 
        JLabel JLabelStartDate = new JLabel("[StartDate]");
        JLabelStartDate.setFont(new Font("Consolas", Font.BOLD, 14));
        JLabelStartDate.setBounds(25, 90, 104, 28);
        contentPane.add(JLabelStartDate);
         
        JLabel JLabelEndDate = new JLabel("[EndDate]");
        JLabelEndDate.setFont(new Font("Consolas", Font.BOLD, 14));
        JLabelEndDate.setBounds(25, 128, 104, 28);
        contentPane.add(JLabelEndDate);
         
        StartDateField = new JTextField();
        StartDateField.setBounds(113, 93, 154, 21);
        contentPane.add(StartDateField);
        StartDateField.setColumns(10);
         
        EndDateField = new JTextField();
        EndDateField.setBounds(113, 131, 154, 21);
        contentPane.add(EndDateField);
        EndDateField.setColumns(10);
        
        final JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        lblNewLabel.setBounds(0, 222, 344, 30);
        contentPane.add(lblNewLabel);
      //确认按钮和监听
        JButton JButtonDetailInquiry = new JButton("Detail");
        JButtonDetailInquiry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	StartDateString =StartDateField.getText();
                EndDateString = EndDateField.getText(); 
                try {
                	Create_Log_File(Fname,StartDateString,EndDateString);
					Datecomparisonmark=CompareStartdateWithEnddate(StartDateString,EndDateString);
					if(Datecomparisonmark==true)
					{
						RunFunction();
					}
					else
					{
						StartDateField.setText(null);
						EndDateField.setText(null);
						StartDateField.requestFocus();
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        JButtonDetailInquiry.setFont(new Font("Consolas", Font.BOLD, 14));
        JButtonDetailInquiry.setBounds(60, 189, 93, 23);
        contentPane.add(JButtonDetailInquiry);
      //重置按钮和监听
        JButton JButtonReset = new JButton("Delete");
        JButtonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	StartDateString =StartDateField.getText();
                EndDateString = EndDateField.getText(); 
                LinkedList<String> deleteAccount=new LinkedList<String>();
                try {
					deleteAccount=GetAccount_No();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                deleteFunction(deleteAccount,StartDateString,EndDateString);
                 
            }
        });
        JButtonReset.setFont(new Font("Consolas", Font.BOLD, 14));
        JButtonReset.setBounds(174, 189, 93, 23);
        contentPane.add(JButtonReset);
        //余额查询
        JButton JButtonBalanceInquiry = new JButton("Combine");
        JButtonBalanceInquiry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	StartDateString =StartDateField.getText();
                EndDateString = EndDateField.getText(); 
            	try {
					SubtotalByCredit_no(StartDateString);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}    
            }
        });
        JButtonBalanceInquiry.setFont(new Font("Consolas", Font.BOLD, 14)); 
        JButtonBalanceInquiry.setBounds(60, 229, 93, 23);
        contentPane.add(JButtonBalanceInquiry);
        //主标签
        JLabel JLabelTitle = new JLabel("CCM(China)CL.BRQ");
        JLabelTitle.setFont(new Font("Consolas", Font.BOLD, 14));
        JLabelTitle.setBounds(123, 45, 200, 28);
        contentPane.add(JLabelTitle);
        
    }
    //删除特定账号在特定日期的记录
    public void deleteFunction(LinkedList<String> LL,String BeginDate,String FinishDate)
    {
    	String Bdate=BeginDate.substring(0, 4)+"-"+BeginDate.substring(4, 6)+"-"+BeginDate.substring(6, 8);
		String Fdate=FinishDate.substring(0, 4)+"-"+FinishDate.substring(4, 6)+"-"+FinishDate.substring(6, 8);
		Connection deleteConn=get_Connection();
		try {
			sql=deleteConn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<LL.size();i++)
		{
			String deleteSQLbdtbt="delete from bdtbt where  TRAN_DATE between DATE_FORMAT('"+Bdate+"','%Y/%m/%d')  and DATE_FORMAT('"+Fdate+"','%Y/%m/%d') and ACC_NO='"+LL.get(i)+"'";
			try 
			{
				sql.executeUpdate(deleteSQLbdtbt);
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int j=0;j<LL.size();j++)
		{
			String deleteSQLbalancebyaccnoandmonth="delete from balancebyaccnoandmonth where  MMonth=month(DATE_FORMAT('"+Bdate+"','%Y/%m/%d'))  and YYear=year(DATE_FORMAT('"+Fdate+"','%Y/%m/%d')) and ACC_NO='"+LL.get(j)+"'";
			try 
			{
				sql.executeUpdate(deleteSQLbalancebyaccnoandmonth);
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
   /**创建请求序列号
   * @return Request_NO
   * @throws InterruptedException
   */
    public String Create_Request_NO() throws InterruptedException
	{
		String Request_NO=null;
		
		int Mathrandom2=(int) ((Math.random()*10)+10);
		int Mathrandom4=(int) ((Math.random()*1000)+1000);
		int Mathrandom5=(int) ((Math.random()*10000)+10000);
		Request_NO=String.valueOf(Mathrandom2)+String.valueOf(Mathrandom4)+String.valueOf(Mathrandom5);
		return Request_NO;
	}
    /**获取账号
     * @return list
     * @throws InterruptedException
     */
    public LinkedList<String> GetAccount_No() throws IOException
	{
		try
		{
			  BufferedReader reader = new BufferedReader(new FileReader("D:\\file\\AccountNo.txt"));
			  String line = null;
			  while ((line = reader.readLine()) != null) 
			  {
				  list.add(line);
			  }
			  reader.close();
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		return list;
	}
    //批量写入
    public void mutipleinsert(LinkedList<String> linkedList,String BeginDate,String FinishDate) throws SQLException
    {
    	System.out.println(linkedList.size());
    	int count=0;
		String AccNoStr=ReadAccNo(fileppath);
		int default_value=0;
		conn=get_Connection();
		PreparedStatement PS;
		PS = conn.prepareStatement(sqlmutiple);
		if(!linkedList.get(0).equals(""))
		{
		for(int j=0;j<linkedList.size();j=j+9)
		{
			System.out.println("开始存储第"+(++count)+"条记录\n");
			TRAN_DATE=linkedList.get(j);
			System.out.println("TRAN_DATE="+TRAN_DATE+"\n");
			CREDIT_NO=linkedList.get(j+1);
			System.out.println("CREDIT_NO="+CREDIT_NO+"\n");
			ABSTRACT=linkedList.get(j+2);
			System.out.println("ABSTRACT="+ABSTRACT+"\n");
			AMOUNT=linkedList.get(j+3);
			System.out.println("AMOUNT="+AMOUNT+"\n");
			BALANCE=linkedList.get(j+4);
			System.out.println("BALANCE="+BALANCE+"\n");
			dORc=linkedList.get(j+5);
			System.out.println("dORc="+dORc+"\n");
			ACC_NAME=linkedList.get(j+6);
			System.out.println("ACC_NAME="+ACC_NAME+"\n");
			INDIVIDUAL=linkedList.get(j+7);
			System.out.println("INDIVIDUAL="+INDIVIDUAL+"\n");
			ID=linkedList.get(j+8);
			System.out.println("ID="+ID+"\n");
			PS.setString(1, CREDIT_NO);
			PS.setString(2, TRAN_DATE);
			PS.setString(3, ABSTRACT);
			if(Integer.valueOf(dORc)==0)
			{
				PS.setString(4, AMOUNT);
				PS.setDouble(5,default_value );
			}
			else
			{
				PS.setDouble(4,default_value );
				PS.setString(5, AMOUNT);
			}
			PS.setString(6, BALANCE);
			PS.setString(7, dORc);
			PS.setString(8, ACC_NAME);
			PS.setString(9,INDIVIDUAL);
			PS.setString(10,ID);
			PS.setString(11,AccNoStr);
			PS.setString(12,AccNoStr+ID);
			PS.addBatch();
		}
		PS.executeBatch();
		conn.close();
		}
    }
    /**读取该账号所返回的记录的总页数
     * @param path
     * @return 报文总页数
     * @throws IOException
     */
    public int ReadPageNum(String path) 
	{
		// TODO Auto-generated method stub
		String subpoststr=null;
		try
		{
			FileReader read1=new FileReader(path);
			BufferedReader BR1=new BufferedReader(read1);
			String row;
			while((row=BR1.readLine())!=null)
			{
				if(row.contains("<TOTAL_PAGE>"))
				{
					String ss1=row;
					int pos1=ss1.indexOf(">");
					int pos2=ss1.indexOf("</");
					subpoststr=ss1.substring(pos1+1,pos2);
					System.out.println("总的报文页数为:"+subpoststr);
				}		
			}
			BR1.close();
		}
		catch(IOException e)
		{
			e.getStackTrace();
		}
		
		return Integer.valueOf(subpoststr);
	}
    /**读取定位串
     * @param path
     * @return 定位串
     * @throws IOException
     */
    public String ReadPostStr(String path) 
	{
		// TODO Auto-generated method stub
		String subpoststr=null;
		try
		{
			FileReader read1=new FileReader(path);
			BufferedReader BR1=new BufferedReader(read1);
			String row;
			while((row=BR1.readLine())!=null)
			{
				if(row.contains("<POSTSTR>"))
				{
					String ss1=row;
					int pos1=ss1.indexOf(">");
					int pos2=ss1.indexOf("</");
					subpoststr=ss1.substring(pos1+1,pos2);
					System.out.println("定位串为:"+subpoststr);
				}		
			}
			BR1.close();
		}
		catch(IOException e)
		{
			e.getStackTrace();
		}
		
		return subpoststr;
	}
    /**发送报文
     * @param temp_Req_Meg1
     * @return 返回的报文
     * @throws Exception
     */
    public String SendMessage(String temp_Req_Meg1) {
		// TODO Auto-generated method stub
		Socket socket=null;
		BufferedReader reader =null;
		PrintWriter writer = null;
		StringBuffer sb =new StringBuffer();
		try 
		{
			socket = new Socket(strHost, intPort);
			socket.setSoTimeout(150*1000);////设置超时时间150秒
		//	// 用得到的会话对象构造输入输出流
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
			writer.println(temp_Req_Meg1);//发送编码后的报文
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
			System.out.println("返回报文："+ ++count +"\r\n"+sb.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("你和服务器断开了，原因:"+e.getMessage());
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
    /**创建每个账号的母报文
     * @param temp_request_no，temp_AccNo，temp_startDate，temp_endDate，temp_pageFirst
     * @return req_meg
     */
    public String Create_Request_Message_ByAccount(String temp_request_no,
			String temp_AccNo, String temp_startDate, String temp_endDate,
			int temp_pageFirst) {
		// TODO Auto-generated method stub
		String req_meg_head="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>";
		String req_meg_accno="</REQUEST_SN><CUST_ID>SHP773718882#001</CUST_ID><USER_ID>WLPT01</USER_ID><PASSWORD>8pmmkm</PASSWORD><TX_CODE>6WY101</TX_CODE><LANGUAGE>CN</LANGUAGE><TX_INFO><ACCNO1>";
		String req_meg_stadate="</ACCNO1><STARTDATE>";
		String req_meg_endate="</STARTDATE><ENDDATE>";
		String req_meg_stapage="</ENDDATE><BARGAIN_FLAG></BARGAIN_FLAG><CHECK_ACC_NO></CHECK_ACC_NO><CHECK_ACC_NAME></CHECK_ACC_NAME><REMARK></REMARK><LOW_AMT></LOW_AMT><HIGH_AMT></HIGH_AMT><PAGE>";
		String req_meg6_tail="</PAGE><POSTSTR></POSTSTR><TOTAL_RECORD>100</TOTAL_RECORD><DET_NO></DET_NO></TX_INFO></TX>";
		if(temp_request_no!=null && temp_AccNo!=null && temp_startDate!=null && temp_endDate!=null && temp_pageFirst!=0)
		{
			req_meg_parent=req_meg_head+temp_request_no+req_meg_accno+temp_AccNo+req_meg_stadate+temp_startDate+req_meg_endate+temp_endDate+req_meg_stapage+String.valueOf(temp_pageFirst)+req_meg6_tail;
		}
		else
		{
			System.out.println("缺少创建母报文的必要元素或创建母报文的必要元素为空");
			req_meg_parent="缺少创建母报文的必要元素或创建母报文的必要元素为空";
		}
		System.out.println("第一份报文的内容为:"+req_meg_parent+"\n");
		return req_meg_parent;
	}
    /**创建每个账号的子报文
     * @param RNo，ANo，SD，ED，pg，PS
     * @return req_meg
     */
    public String Create_Request_Message_InnerEveryAccount(String RNo,String ANo,String SD,String ED,int pg,String PS)
	{
		String req_meg_child_head="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>";
		String req_meg_child_accno="</REQUEST_SN><CUST_ID>SHP773718882#001</CUST_ID><USER_ID>WLPT01</USER_ID><PASSWORD>8pmmkm</PASSWORD><TX_CODE>6WY101</TX_CODE><LANGUAGE>CN</LANGUAGE><TX_INFO><ACCNO1>";
		String req_meg_child_stadate="</ACCNO1><STARTDATE>";
		String req_meg_child_endate="</STARTDATE><ENDDATE>";
		String req_meg_child_stapage="</ENDDATE><BARGAIN_FLAG></BARGAIN_FLAG><CHECK_ACC_NO></CHECK_ACC_NO><CHECK_ACC_NAME></CHECK_ACC_NAME><REMARK></REMARK><LOW_AMT></LOW_AMT><HIGH_AMT></HIGH_AMT><PAGE>";
		String req_meg_child_poststr="</PAGE><POSTSTR>";
		String req_meg_child_tail="</POSTSTR><TOTAL_RECORD>100</TOTAL_RECORD><DET_NO></DET_NO></TX_INFO></TX>";
		if(RNo!=null && ANo!=null && SD!=null && SD!=null && pg>1 && PS!=null)
		{
			req_meg_child=req_meg_child_head+RNo+req_meg_child_accno+ANo+req_meg_child_stadate+SD+req_meg_child_endate+ED+req_meg_child_stapage+String.valueOf(pg)+req_meg_child_poststr+PS+req_meg_child_tail;
		}
		else
		{
			System.out.println("缺少创建子报文的必要元素或创建子报文的必要元素为空");
			req_meg_child="缺少创建子报文的必要元素或创建子报文的必要元素为空";
		}
		
		System.out.println("子报文的内容为:"+req_meg_child+"\n");
		return req_meg_child;
	}
    /**连接mysql数据库
     * @param 
     * @return Connection
     */
    public static Connection get_Connection() {
		// TODO Auto-generated method stub
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功");
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return conn;
	}
    /**读取账户名称
     * @param fileppath
     * @return String
     */
    public String ReadAccNo(String fileppath) 
	{
		String subpoststr=null;
		try
		{
			FileReader read1=new FileReader(fileppath);
			BufferedReader BR1=new BufferedReader(read1);
			String row;
			while((row=BR1.readLine())!=null)
			{
				if(row.contains(Str1))
				{
					String ss1=row;
					int pos1=ss1.indexOf(">");
					int pos2=ss1.indexOf("</");
					subpoststr=ss1.substring(pos1+1,pos2);
					System.out.println("账户为"+subpoststr+"\n");
				}		
			}
			BR1.close();
		}
		catch(IOException e)
		{
			e.getStackTrace();
		}
		return subpoststr;
	}
    /**读出返回所需内容
     * @param fileppath
     * @return LinkedList<String>
     */
    public LinkedList<String> ReadContent(String strfilepath)
	{
		ListLine=new LinkedList<String>();
		System.out.println("数组的开始长度为："+ListLine.size());
		try
		{
			FileReader read1=new FileReader(strfilepath);
			BufferedReader BR1=new BufferedReader(read1);
			String row;
			while((row=BR1.readLine())!=null)
			{
				
				if(row.contains(Str3))//取凭证号
				{
					//String ss=null;
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					CREDIT_NO=row.substring(pos1+1,pos2);
					if(CREDIT_NO!=null)
					{
						ListLine.add(CREDIT_NO);
					}
					else
					{
						CREDIT_NO="null";
						ListLine.add(CREDIT_NO);
					}
					System.out.println("CREDIT_NO="+CREDIT_NO);
					
				}
				if(row.contains(Str2))//取交易日期
				{
					//String ss=null;
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					TRAN_DATE=row.substring(pos1+1,pos2);
					
					if(TRAN_DATE!=null)
					{
						ListLine.add(TRAN_DATE);
					}
					else
					{
						TRAN_DATE="null";
						ListLine.add(TRAN_DATE);
					}
					System.out.println("TRAN_DATE="+TRAN_DATE);
				}
				if(row.contains(Str4))//取摘要
				{
					//String ss=null;
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					ABSTRACT=row.substring(pos1+1,pos2);
					if(ABSTRACT!=null)
					{
						ListLine.add(ABSTRACT);
					}
					else
					{
						ABSTRACT="null";
						ListLine.add(ABSTRACT);
					}
					
					System.out.println("ABSTRACT="+ABSTRACT);
				}
				if(row.contains(Str5))//取交易额
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					AMOUNT=row.substring(pos1+1,pos2);
					if(AMOUNT!=null)
					{
						ListLine.add(AMOUNT);
					}
					else
					{
						AMOUNT="null";
						ListLine.add(AMOUNT);
					}
					
					System.out.println("AMOUNT="+AMOUNT);
				}
				if(row.contains(Str6))//取余额
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					BALANCE=row.substring(pos1+1,pos2);
					if(BALANCE!=null)
					{
						ListLine.add(BALANCE);
					}
					else
					{
						BALANCE="null";
						ListLine.add(BALANCE);
					}
					
					System.out.println("BALANCE="+BALANCE);
				}
				if(row.contains(Str7))//取借贷标志
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					dORc=row.substring(pos1+1,pos2);
					if(dORc!=null)
					{
						ListLine.add(dORc);
					}
					else
					{
						dORc="null";
						ListLine.add(dORc);
					}
					
					System.out.println("dORc="+dORc);
				}
				if(row.contains(Str8))//取对方账户名称
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					ACC_NAME=row.substring(pos1+1,pos2);
					if(ACC_NAME!=null)
					{
						ListLine.add(ACC_NAME);
					}
					else
					{
						ACC_NAME="null";
						ListLine.add(ACC_NAME);
					}
					
					System.out.println("ACC_NAME="+ACC_NAME);
				}
				if(row.contains(Str9))//取备注1
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					INDIVIDUAL=row.substring(pos1+1,pos2);
					if(INDIVIDUAL!=null)
					{
						ListLine.add(INDIVIDUAL);
					}
					else
					{
						INDIVIDUAL="null";
						ListLine.add(INDIVIDUAL);
					}
					
					System.out.println("INDIVIDUAL="+INDIVIDUAL);
				}
				
				if(row.contains(Str10))
				{
					
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					String ID3 =row.substring(pos1+1,pos2);
					
					if( !ID3.equals("") )
					{
						ID=ID3;
						ListLine.add(ID);	
					}
					else
					{
						ID=String.valueOf((int)(Math.random()*1000000)+1000000);
						ListLine.add(ID);
					}
					System.out.println("ID="+ID);
				}			
			}
			BR1.close();
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		System.out.println("数组存储数据后的长度为："+ListLine.size());
		for(int mm=0;mm<ListLine.size();mm=mm+9)
		{
			System.out.println("交易日期为:"+ListLine.get(mm)+"\n");
			System.out.println("凭证号为:"+ListLine.get(mm+1)+"\n");
			System.out.println("摘要为:"+ListLine.get(mm+2)+"\n");
			System.out.println("交易额为:"+ListLine.get(mm+3)+"\n");
			System.out.println("余额为:"+ListLine.get(mm+4)+"\n");
			System.out.println("借贷标志为:"+ListLine.get(mm+5)+"\n");
			System.out.println("对方账户为:"+ListLine.get(mm+6)+"\n");
			System.out.println("备注为为:"+ListLine.get(mm+7)+"\n");
			System.out.println("ID为:"+ListLine.get(mm+8)+"\n");
		}
	return ListLine;
	}
    /**清除文件内容
     * @param temp_file_path
     * @return 
	 *@throws IOException
     */
    public void Empty_file(String temp_file_path) throws IOException 
	{
		File temp_file=new File(temp_file_path);
		// TODO Auto-generated method stub
		if(temp_file.exists())
		{
			FileOutputStream fileoutputstream=new FileOutputStream(temp_file.getPath());
			fileoutputstream.write(new String("").getBytes());
			fileoutputstream.close();
		}
	}
    /**判断返回信息是否为交易记录
     * @param fileppath
     * @return boolean
	 *@throws Exception
     */
    public boolean IsRecord(String fileppath)
	{
		boolean falsg = false;
		try
		{
			FileReader read=new FileReader(fileppath);
			BufferedReader BR=new BufferedReader(read);
			String row;
			while((row=BR.readLine())!=null)
			{
				if(row.contains("<RETURN_MSG>"))
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					RETURN_MSG=row.substring(pos1+1,pos2);
					falsg=isContainChinese(RETURN_MSG);
				}
			}
			BR.close();
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		return falsg;
	}
    /**判断报文中是否包含了汉字
     * @param fileppath
     * @return boolean
	 *@throws
     */
    public  boolean isContainChinese(String str) 
	{

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
    /**运行函数
     * @param fileppath
     * @return boolean
	 *@throws
     */
    public void RunFunction()
    {
		try 
		{
			Empty_file(fileppath);
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		LinkedList<String> Temp_list = new LinkedList<String>();
		try {
			Temp_list=GetAccount_No();
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		String Temp_request_no=null;
		String Temp_AccNo=null;
		String Temp_Req_Meg1=null;
		String Temp_Req_Meg2=null;
		String Temp_startDate=null;
		String Temp_endDate=null;
		String BackMessage=null;
		int Temp_pageFirst;
		Temp_startDate=StartDateString;//开始日期
		System.out.println(Temp_startDate);
		Temp_endDate=EndDateString;//结束日期
		System.out.println(Temp_endDate);		
		File Temp_file=new File(fileppath);	
		System.out.println("账号的长度为："+Temp_list.size());
		for(int i=0;i<Temp_list.size();i++)
		{
			
			Temp_AccNo=Temp_list.get(i);//获取每一个账号
			System.out.println(Temp_AccNo);
			try 
			{
				Temp_request_no=Create_Request_NO();
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Temp_pageFirst=1;//每个账号的第一次请求页数从1开始
			Temp_Req_Meg1=Create_Request_Message_ByAccount(Temp_request_no,Temp_AccNo,Temp_startDate,Temp_endDate,Temp_pageFirst);
			System.out.println("将要发送的报文为:"+Temp_Req_Meg1);
			BackMessage=SendMessage(Temp_Req_Meg1);
			try
			{
				if(!Temp_file.exists())
				{
					Temp_file.createNewFile();
					String temp="";
					FileInputStream FIS=null;
					InputStreamReader ISR=null;
					BufferedReader BR=null;
					FileOutputStream FOS=null;
					PrintWriter PW=null;
					try
					{
						FIS=new FileInputStream(Temp_file);
						ISR=new InputStreamReader(FIS);
						BR=new  BufferedReader(ISR);
						StringBuffer BUF=new StringBuffer();
						while(BR.readLine()!=null)
						{
							BUF=BUF.append(temp);
							BUF=BUF.append(System.getProperty("line.separator"));
						}
						BUF.append(BackMessage.toString());
						FOS=new FileOutputStream(Temp_file);
						PW=new PrintWriter(FOS);
						PW.write(BUF.toString().toCharArray());
						PW.flush();
					}
					catch(IIOException e)
					{
						e.getStackTrace();
					}
					catch(IOException e)
					{
						e.getStackTrace();
					}
					finally
					{
						if(PW!=null)
						{
							PW.close();
						}
						if(FOS!=null)
						{
							FOS.close();
						}
						if(BR!=null)
						{
							BR.close();
						}
						if(ISR!=null)
						{
							ISR.close();
						}
						if(FIS!=null)
						{
							FIS.close();
						}
					}	
				}
				else
				{
					String temp="";
					FileInputStream FIS=null;
					InputStreamReader ISR=null;
					BufferedReader BR=null;
					FileOutputStream FOS=null;
					PrintWriter PW=null;
					try
					{
						FIS=new FileInputStream(Temp_file);
						ISR=new InputStreamReader(FIS);
						BR=new  BufferedReader(ISR);
						StringBuffer BUF=new StringBuffer();
						while(BR.readLine()!=null)
						{
							BUF=BUF.append(temp);
							BUF=BUF.append(System.getProperty("line.separator"));
						}
						BUF.append(BackMessage.toString());
						FOS=new FileOutputStream(Temp_file);
						PW=new PrintWriter(FOS);
						PW.write(BUF.toString().toCharArray());
						PW.flush();
					}
					catch(IIOException e)
					{
						e.getStackTrace();
					}
					catch(IOException e)
					{
						e.getStackTrace();
					}
					finally
					{
						if(PW!=null)
						{
							PW.close();
						}
						if(FOS!=null)
						{
							FOS.close();
						}
						if(BR!=null)
						{
							BR.close();
						}
						if(ISR!=null)
						{
							ISR.close();
						}
						if(FIS!=null)
						{
							FIS.close();
						}
					}
				}
			}
			catch(IOException e)
			{
				e.getStackTrace();
			}
			NextFlag=IsRecord(fileppath);
			if(NextFlag==false)
			{
				System.out.println("解析第1条报文:\n");
				ReadAccNo(fileppath);
				PageNum=ReadPageNum(Temp_file.getPath());
				PostStr=ReadPostStr(Temp_file.getPath());
				if(PageNum==1 || PageNum==0)
				{
					getBalanceByAccnoAndMonth(ReadAccNo(fileppath),fileppath,EndDateString);
				}
				try 
				{
					mutipleinsert(ReadContent(fileppath),StartDateString,EndDateString);
				} catch (SQLException e3) 
				{
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				if(PageNum>1)
				{
					try 
					{
						Empty_file(fileppath);
					} 
					catch (IOException e2) 
					{
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					for(int i1=0;i1<PageNum-1;i1++)
					{
						
						try 
						{
							Temp_request_no=null;
							Temp_request_no=Create_Request_NO();
						} 
						catch (InterruptedException e) 
						{
								// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Temp_Req_Meg2=Create_Request_Message_InnerEveryAccount(Temp_request_no, Temp_AccNo, Temp_startDate, Temp_endDate, i1+2,PostStr);
						BackMessage=SendMessage(Temp_Req_Meg2);
						try
						{
							if(!Temp_file.exists())
							{
								Temp_file.createNewFile();
								String temp="";
								FileInputStream FIS=null;
								InputStreamReader ISR=null;
								BufferedReader BR=null;
								FileOutputStream FOS=null;
								PrintWriter PW=null;
								try
								{
									FIS=new FileInputStream(Temp_file);
									ISR=new InputStreamReader(FIS);
									BR=new  BufferedReader(ISR);
									StringBuffer BUF=new StringBuffer();
									while(BR.readLine()!=null)
									{
										BUF=BUF.append(temp);
										BUF=BUF.append(System.getProperty("line.separator"));
									}
									BUF.append(BackMessage.toString());
									FOS=new FileOutputStream(Temp_file);
									PW=new PrintWriter(FOS);
									PW.write(BUF.toString().toCharArray());
									PW.flush();
								}
								catch(IIOException e)
								{
									e.getStackTrace();
								}
								catch(IOException e)
								{
									e.getStackTrace();
								}
								finally
								{
									if(PW!=null)
									{
										PW.close();
									}
									if(FOS!=null)
									{
										FOS.close();
									}
									if(BR!=null)
									{
										BR.close();
									}
									if(ISR!=null)
									{
										ISR.close();
									}
									if(FIS!=null)
									{
										FIS.close();
									}
								}
							}
							else
							{
								String temp="";
								FileInputStream FIS=null;
								InputStreamReader ISR=null;
								BufferedReader BR=null;
								FileOutputStream FOS=null;
								PrintWriter PW=null;
								try
								{
									FIS=new FileInputStream(Temp_file);
									ISR=new InputStreamReader(FIS);
									BR=new  BufferedReader(ISR);
									StringBuffer BUF=new StringBuffer();
									while(BR.readLine()!=null)
									{
										BUF=BUF.append(temp);
										BUF=BUF.append(System.getProperty("line.separator"));
									}
									BUF.append(BackMessage.toString());
									FOS=new FileOutputStream(Temp_file);
									PW=new PrintWriter(FOS);
									PW.write(BUF.toString().toCharArray());
									PW.flush();
								}
								catch(IIOException e)
								{
									e.getStackTrace();
								}
								catch(IOException e)
								{
									e.getStackTrace();
								}
								finally
								{
									if(PW!=null)
									{
										PW.close();
									}
									if(FOS!=null)
									{
										FOS.close();
									}
									if(BR!=null)
									{
										BR.close();
									}
									if(ISR!=null)
									{
										ISR.close();
									}
									if(FIS!=null)
									{
										FIS.close();
									}
								}
							}							
						}
						catch(IOException e)
						{
							e.getStackTrace();
						}
						NextFlag=IsRecord(fileppath);
						if(NextFlag==false)
						{
							
							System.out.println("开始解析第"+(i1+2)+"条报文:\n");
							ReadAccNo(fileppath);
							if(i1==PageNum-2)
							{
								getBalanceByAccnoAndMonth(ReadAccNo(fileppath),fileppath,EndDateString);
							}
							try {
								mutipleinsert(ReadContent(fileppath),StartDateString,EndDateString);
								try {
									Empty_file(fileppath);
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try 
							{
								Empty_file(fileppath);
							} 
							catch (IOException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try 
							{
								Thread.sleep(500);
							} 
							catch (InterruptedException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								Empty_file(fileppath);
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}	
					}
				}
			}
			else
			{
				//读出返回信息和账号写入日志
				String RM=Get_ReturnedMessage(fileppath);
				System.out.println("我是返回消息："+RM);
				System.out.println("我是银行账号："+Temp_AccNo);
				try {
					try {
						Append_Content_To_LOG(Fname,Temp_AccNo,RM);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Empty_file(fileppath);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
		//try {
			//SubtotalByCredit_no();
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		JOptionPane.showMessageDialog(this,"Tip: System Running End!");
    }
    /**日期比较
     * @param startdate，enddate
     * @return boolean
	 *@throws ParseException
     */
    public boolean CompareStartdateWithEnddate(String startdate,String enddate) throws ParseException
    {
    	String LinkDate1=startdate.subSequence(0, 4)+"-"+startdate.subSequence(4, 6)+"-"+startdate.substring(6, 8);
    	String LinkDate2=enddate.subSequence(0, 4)+"-"+enddate.subSequence(4, 6)+"-"+enddate.substring(6, 8);
    	DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date DateCompare1=dateformat.parse(LinkDate1); 
		Date DateCompare2=dateformat.parse(LinkDate2);
		if(DateCompare1.getTime()>DateCompare2.getTime())
		{
			JOptionPane.showMessageDialog(this,"Warning: Startdate Can Not Be Greater Than The EndDate, Please Re - Enter.");
			System.out.println(startdate+">"+enddate);
			return false;
		}
		else
		{
			System.out.println(startdate+"<"+enddate);
			return true;
		}
    }
    /**生成日志文件
     * @param Ret_Msg,Acc_No,Sta_Date,End_Date
     * @return boolean
	 *@throws ParseException
     */
    public void Create_Log_File(String name,String Sta_Date,String End_Date)
    {
    	//指定目录下生成一个日志文件
	    File Temp_file=new File(name);	
	    FileWriter fw;
	    try {
		fw = new   FileWriter(Temp_file);
		BufferedWriter   bw=new   BufferedWriter(fw);   
		bw.write("查询开始日期:"+Sta_Date);   
		bw.newLine();   
		bw.write("查询结束日期:"+End_Date);   
		bw.newLine(); 
		bw.close();
		fw.close();
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}   
    }
    /**给日志文件追加内容
     * @param filepath,accountnumber,returnmessage
     * @return boolean
     * @throws ParserConfigurationException 
     * @throws TransformerException 
	 *@throws ParseException
     */
    public void Append_Content_To_LOG(String name,String accountnumber,String returnmessage) throws ParserConfigurationException, TransformerException, IOException
    {
    	FileWriter fw=new FileWriter(name,true);
    	fw.write(accountnumber);
    	fw.write(returnmessage);
    	fw.close();		
    }
    /**读取返回信息
     * @param filepath
     * @return return_message
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public String Get_ReturnedMessage(String fileppath)
    {
    	String ReturnedMessage=null;
    	try
		{
			FileReader read=new FileReader(fileppath);
			BufferedReader BR=new BufferedReader(read);
			String row;
			while((row=BR.readLine())!=null)
			{
				if(row.contains("<RETURN_MSG>"))
				{
					int pos1=row.indexOf(">");
					int pos2=row.indexOf("</");
					ReturnedMessage=row.substring(pos1+1,pos2);
				}
			}
			BR.close();
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
    	return ReturnedMessage;
    }
    /**创建余额查询报文
     * @param reqno,accno
     * @return ReturnedMessage
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public String ReturnedBalanceMessage(String reqno,String accno)
    {
    	String ReturnedMessage=null;
    	String SubMessage1="<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><TX><REQUEST_SN>";
    	String SubMessage2="</REQUEST_SN><CUST_ID>SHP773718882#001</CUST_ID><USER_ID>WLPT01</USER_ID><PASSWORD>8pmmkm</PASSWORD><TX_CODE>6W0100</TX_CODE><LANGUAGE>CN</LANGUAGE><TX_INFO><ACC_NO>";
    	String SubMessage3="</ACC_NO></TX_INFO></TX>";
    	ReturnedMessage=SubMessage1+reqno+SubMessage2+accno+SubMessage3;
    	return ReturnedMessage;
    }
    /**解析余额报文
     * @param BalanceFilePath
     * @return MiddleLinkList
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public LinkedList<String> Get_Balance(String BalanceFilePath)
    {
    	String AccNo="<ACC_NO>";
    	String Balance="<BALANCE>";
    	String Balance1="<BALANCE1>";
    	String TempAccNo=null;
    	String TempBalance=null;
    	String TempBalance1=null;
    	LinkedList<String> MiddleLinkList=new LinkedList<String>();
		try {
			FileReader filereader = new FileReader(BalanceFilePath);
			BufferedReader bufferedreader=new BufferedReader(filereader);
			String row=null;
			try {
				while((row=bufferedreader.readLine())!=null)
				{
					
					if(row.contains(AccNo))//取账号
					{
						int pos1=row.indexOf(">");
						int pos2=row.indexOf("</");
						TempAccNo=row.substring(pos1+1,pos2);
						if(TempAccNo!=null)
						{
							MiddleLinkList.add(TempAccNo);
						}	
					}
					if(row.contains(Balance))//取余额
					{
						int pos1=row.indexOf(">");
						int pos2=row.indexOf("</");
						TempBalance=row.substring(pos1+1,pos2);
						if(TempBalance!=null)
						{
							MiddleLinkList.add(TempBalance);
						}	
					}
					if(row.contains(Balance1))//取可用余额
					{
						int pos1=row.indexOf(">");
						int pos2=row.indexOf("</");
						TempBalance1=row.substring(pos1+1,pos2);
						if(TempBalance1!=null)
						{
							MiddleLinkList.add(TempBalance1);
						}	
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MiddleLinkList;
    }
    /**余额入库
     * @param linklist
     * @return 
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public void WriteBalanceToDB(LinkedList<String> linklist)
    {
    	Connection c=get_Connection();
    	String AboutInsertAccno=null;
    	String AboutInsertBalance=null;
    	String AboutInsertBalance1=null;
    	Statement statement=null;
    	for(int LoopParameter=0;LoopParameter<linklist.size();LoopParameter=LoopParameter+3)
    	{
    		AboutInsertAccno=linklist.get(LoopParameter);
    		AboutInsertBalance=linklist.get(LoopParameter+1);
    		AboutInsertBalance1=linklist.get(LoopParameter+2);
    		try {
    			try {
    				statement=c.createStatement();
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			statement.executeUpdate("insert into accno_balac(Acc_No,Balance,Balance1)values('"+AboutInsertAccno+"','"+AboutInsertBalance+"','"+AboutInsertBalance1+"')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**清空余额表
     * @param linklist
     * @return 
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public void DeleteBalance() throws SQLException
    {
    	conn=get_Connection();
		sql=conn.createStatement();
		String deleteSQL="delete from balancebyaccnoandmonth";
		try {
			sql.executeUpdate(deleteSQL);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void RunBalanceInqiry()
    {
    	try {
			DeleteBalance();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	System.out.println("开始查询余额。。。");
    	String BalanceResultFilePath="D:\\file\\Balance.txt";
    	LinkedList<String> accList=new LinkedList<String>();
    	LinkedList<String> BalanceList=new LinkedList<String>();
    	String Qaxlh=null;
    	try {
			accList=GetAccount_No();
			System.out.println("账号获取完毕。。。");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			Qaxlh=Create_Request_NO();
			System.out.println("序列号获取完毕。。。");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String sendmessage=null;
    	String responsemessage=null;
    	for(int AccountNumberLength=0;AccountNumberLength<accList.size();AccountNumberLength++)
    	{
    		sendmessage=ReturnedBalanceMessage(Qaxlh,accList.get(AccountNumberLength));
    		System.out.println("报文发送完毕。。。");
    		responsemessage=SendMessage(sendmessage);
    		System.out.println("获取返回报文。。。");
    		File Temfile=new File(BalanceResultFilePath);
    		if(!Temfile.exists())
			{
    			try {
					Temfile.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String temp="";
				FileInputStream FIS=null;
				InputStreamReader ISR=null;
				BufferedReader BR=null;
				FileOutputStream FOS=null;
				PrintWriter PW=null;
				try
				{
					FIS=new FileInputStream(Temfile);
					ISR=new InputStreamReader(FIS);
					BR=new  BufferedReader(ISR);
					StringBuffer BUF=new StringBuffer();
					while(BR.readLine()!=null)
					{
						BUF=BUF.append(temp);
						BUF=BUF.append(System.getProperty("line.separator"));
					}
					BUF.append(responsemessage.toString());
					FOS=new FileOutputStream(Temfile);
					PW=new PrintWriter(FOS);
					PW.write(BUF.toString().toCharArray());
					PW.flush();
				}
				catch(IIOException e)
				{
					e.getStackTrace();
				}
				catch(IOException e)
				{
					e.getStackTrace();
				}
				finally
				{
					if(PW!=null)
					{
						PW.close();
					}
					if(FOS!=null)
					{
						try {
							FOS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(BR!=null)
					{
						try {
							BR.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ISR!=null)
					{
						try {
							ISR.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(FIS!=null)
					{
						try {
							FIS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}	
			}
			else
			{
				String temp="";
				FileInputStream FIS=null;
				InputStreamReader ISR=null;
				BufferedReader BR=null;
				FileOutputStream FOS=null;
				PrintWriter PW=null;
				try
				{
					FIS=new FileInputStream(Temfile);
					ISR=new InputStreamReader(FIS);
					BR=new  BufferedReader(ISR);
					StringBuffer BUF=new StringBuffer();
					while(BR.readLine()!=null)
					{
						BUF=BUF.append(temp);
						BUF=BUF.append(System.getProperty("line.separator"));
					}
					BUF.append(responsemessage.toString());
					FOS=new FileOutputStream(Temfile);
					PW=new PrintWriter(FOS);
					PW.write(BUF.toString().toCharArray());
					PW.flush();
				}
				catch(IIOException e)
				{
					e.getStackTrace();
				}
				catch(IOException e)
				{
					e.getStackTrace();
				}
				finally
				{
					if(PW!=null)
					{
						PW.close();
					}
					if(FOS!=null)
					{
						try {
							FOS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(BR!=null)
					{
						try {
							BR.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ISR!=null)
					{
						try {
							ISR.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(FIS!=null)
					{
						try {
							FIS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
    		System.out.println("存储完毕。。。。");
    		BalanceList=Get_Balance(BalanceResultFilePath);
    		for(int len=0;len<BalanceList.size();len++)
    		{
    			System.out.println("哈哈，我在这里："+BalanceList.get(len));
    		}
    		WriteBalanceToDB(BalanceList);
    	}
    }
    /**银行余额按账户按月存放
     * @param account_number,filepath,endate
     * @return void
     * @throws  
     * @throws  
     * @throws  
	 *@throws 
     */
    public void getBalanceByAccnoAndMonth(String AN,String fp,String Edate)
    {
    	LinkedList<String> BalanaceList=new LinkedList<String>();
    	BalanaceList=ReadContent(fp);
    	Statement BalanceSta1=null;
    	Statement BalanceBigAccNu=null;
    	int index1=(BalanaceList.size()/9-1)*9;
    	int index2=(BalanaceList.size()/9-1)*9+4;
    	int Eday=Integer.valueOf(Edate.substring(6, 8));
    	if(BalanaceList.get(index1).length()!=0 && BalanaceList.get(index2).length()!=0)
    	{
    	String YY=BalanaceList.get(index1).substring(0, 4);
    	System.out.println("YY="+YY);
    	int MM=Integer.valueOf(BalanaceList.get(index1).substring(5, 7));
    	System.out.println("MM="+YY);
    	String BB=BalanaceList.get(index2);
    	System.out.println("账号是:"+AN);
    	System.out.println("余额是:"+BB);
    	Connection Co=get_Connection();
    	if(AN.equals("31001505400050026898")||AN.equals("31001505400050027037")||AN.equals("31001505400050026913")||AN.equals("51001468308051508918")||AN.equals("31001505400050026463")||AN.equals("31001505400050026797")||AN.equals("31001505400050026906"))
    	{
    		try {
				BalanceBigAccNu=Co.createStatement();
				if(Eday==30 || Eday==15 || Eday==10 || Eday==7 || Eday==5 || Eday==3 || Eday==3 ||  Eday==1)
	    		{
					BalanceBigAccNu.executeUpdate("insert into balancebyaccnoandmonth(AccountNumber,YYear,MMonth,Balance)values('"+AN+"','"+YY+"','"+MM+"','"+BB+"')");
	    		}
				else
				{
					BalanceBigAccNu.executeUpdate("update balancebyaccnoandmonth set Balance='"+BB+"' where AccountNumber='"+AN+"' and MMonth='"+MM+"' and YYear='"+YY+"'");
				}
			} catch (SQLException e) {
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else
    	{
        	try {
    				BalanceSta1=Co.createStatement();
    				BalanceSta1.executeUpdate("insert into balancebyaccnoandmonth(AccountNumber,YYear,MMonth,Balance)values('"+AN+"','"+YY+"','"+MM+"','"+BB+"')");
    			}
        	 catch (SQLException e) {
    				// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    	try {
			Co.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	else
    	{
    		String rm="改账号在本月无交易";
    		try {
				Append_Content_To_LOG(Fname,AN,rm);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }

    /**按照凭证号码进行合并
     * @param 
     * @return void
     * @throws  SQLException
     * @throws  
     * @throws  
	 *@throws 
     */
    public void SubtotalByCredit_no(String endate) throws SQLException
    {
    	sql=null;
    	Connection ConSubtotal=null;
    	ConSubtotal=get_Connection();
    	sql=ConSubtotal.createStatement();
    	ResultSet RS=null;
    	//int mmonth=Integer.valueOf(endate.substring(4, 6));
    	//System.out.println(mmonth);
    	//String yyear=endate.substring(0, 4);
    	//System.out.println(yyear);
    	RS=sql.executeQuery("select table1.CREDIT_NO,max(table1.TRAN_DATE) HZD,SUM(table1.LOAN_MONEY) AS DHZ,table1.ACC_NO,table1.ACC_NAME from(select * from bdtbt where month(TRAN_DATE)=MONTH(DATE_FORMAT('"+endate+"','%Y/%m/%d')) and year(TRAN_DATE)=year(DATE_FORMAT('"+endate+"','%Y/%m/%d')) and ( ACC_NAME = '待清算直联商户消费款户' or CREDIT_NO like '9553301190%' or CREDIT_NO like '6232511190%')) as table1 group by table1.CREDIT_NO");
    	Statement Sta=ConSubtotal.createStatement();
		String sqlString="delete from bdtbt where month(TRAN_DATE)=MONTH(DATE_FORMAT('"+endate+"','%Y/%m/%d')) and year(TRAN_DATE)=year(DATE_FORMAT('"+endate+"','%Y/%m/%d'))  and ( ACC_NAME = '待清算直联商户消费款户' or CREDIT_NO like '9553301190%' or CREDIT_NO like '6232511190%')";
		Sta.executeUpdate(sqlString);
    	int _replacevalue=0;
    	int _dorc=1;
    	System.out.println("进入合并函数");
    	int record=0;
    	while(RS.next()==true)
    	{
    		record++;
    		System.out.println("这是第"+record+"条记录");
    		System.out.println("凭证号码为:"+RS.getString("CREDIT_NO"));
    		System.out.println("汇总时间为:"+RS.getString("HZD"));
    		System.out.println("汇总贷款为:"+RS.getString("DHZ"));
    		System.out.println("对应账号为:"+RS.getString("ACC_NO"));
    		int Mathrandom=(int) ((Math.random()*10000)+10000);
			String uniqueString=RS.getString("ACC_NO")+String.valueOf(Mathrandom);
    		Statement _statement=ConSubtotal.createStatement();
			String _sqlString="insert into bdtbt(CREDIT_NO,TRAN_DATE,ABSTRACT,BORROW_MONEY,LOAN_MONEY,BALANCE,dORc,ACC_NO,Unique_Index)values('"+RS.getString("CREDIT_NO")+"','"+RS.getString("HZD")+"','"+RS.getString("CREDIT_NO")+"','"+_replacevalue+"','"+RS.getString("DHZ")+"','"+_replacevalue+"','"+_dorc+"','"+RS.getString("ACC_NO")+"','"+uniqueString+"')";
			_statement.executeUpdate(_sqlString);
    	}
    	RS.close();
    	ConSubtotal.close();
    }
    public static void main(String[] args) 
    {
        ManJiTianPin frame = new ManJiTianPin();
        frame.setVisible(true);    
    }
}



