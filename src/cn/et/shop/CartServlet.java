package cn.et.shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.et.shop.utils.DBUtils;



public class CartServlet extends HttpServlet {

	/*
	 * ��ֵ��
	 * id=����
	 * �����ĳ����Ʒ�� ���session�� ������ ��id�ļ�¼ id=1
	 *							  ���ڸ�id�ļ�¼ id=id+1
	 */
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String id=request.getParameter("add");
		String did=request.getParameter("delete");
		HttpSession session=request.getSession();
		//���ﳵ���㷨
		if(did!=null){
			int bn =(Integer)session.getAttribute(did)-1;
			session.setAttribute(did, bn);
			if(bn<=0){
				session.removeAttribute(did);
			}
		}else if(id!=null){
			if(session.getAttribute(id)==null){
				session.setAttribute(id, 1);
			}else{
				session.setAttribute(id, (Integer)session.getAttribute(id)+1);
			}
			
		}
		
		
		out.println("<a href='query'>��������</a>");
		out.println("�������ݣ�");
		out.println("<style type=\"text/css\">"+
				   "table{"+
							"text-align:center;width:100%;border: 1px solid black"+
						"}"+
				   "th,td{"+
							"text-align:center;border: 1px solid black"+
						"}</style>");
		out.println("<table>"+
		    	"<tr>"+
		    		"<th>ID</th><th>��ʾͼƬ</th><th>��Ʒ����</th><th>��Ʒ����</th><th>��Ʒ����</th>"+
		    	"</tr>");
		
		Enumeration em=session.getAttributeNames();
//		while (em.hasMoreElements()) {
//			String name=em.nextElement().toString();
//			String value=session.getAttribute(name).toString();
//			out.println(
//			    	"<tr>"+
//			    		"<td>"+name+"</td><td>"+value+"</td><td>����</td>"+
//			    	"</tr>");
//		}
		
		try {
			PreparedStatement sta;
			ResultSet rs;
			//session����ID
			while(em.hasMoreElements()){
				//��ȡID
				String key=em.nextElement().toString();
				//ID�Ķ�Ӧ�Ĵ���
				String value=session.getAttribute(key).toString();
				String sql="select * from goods where id="+key;
				sta=conn.prepareStatement(sql);
				rs=sta.executeQuery();
				if(rs.next()){
					String name=rs.getString("NAME");
					String imagePath=rs.getString("IMAGEPATH");
					String price=rs.getString("PRICE");
					out.println(
				    	"<tr>"+
				    		"<td>"+key+"</td><td><img src=\""+request.getContextPath()+imagePath+"\"></td>"+
				    		"<td>"+name+"</td><td>"+price+"</td>" +
				    		"<td>"+"<input type=\"button\" value=\"+\" onclick=\"window.location='cart?add="+key+"'\" />"+value+
				    		"<input type='text' size='4px' id="+key+" value='"+value+"'"+"onkeydown=\"if(event.keyCode == 13)" +
							"{window.location='cart?enter="+key+"&num='+document.getElementById('"+key+"').value}\" />"+
				    		"<input type=\"button\" value=\"-\" onclick=\"window.location='cart?delete="+key+"'\" /></td>" +
				    	"</tr>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("</table>");
		
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
	public Connection conn;
	@Override
	public void init() throws ServletException {
		try {
			conn=DBUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.init();
	}
}
