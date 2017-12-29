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
	 * 键值对
	 * id=次数
	 * 点击了某个商品后 如果session中 不存在 该id的记录 id=1
	 *							  存在该id的记录 id=id+1
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
		//购物车的算法
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
		
		
		out.println("<a href='query'>继续购物</a>");
		out.println("购物数据：");
		out.println("<style type=\"text/css\">"+
				   "table{"+
							"text-align:center;width:100%;border: 1px solid black"+
						"}"+
				   "th,td{"+
							"text-align:center;border: 1px solid black"+
						"}</style>");
		out.println("<table>"+
		    	"<tr>"+
		    		"<th>ID</th><th>显示图片</th><th>商品名称</th><th>商品单价</th><th>商品数量</th>"+
		    	"</tr>");
		
		Enumeration em=session.getAttributeNames();
//		while (em.hasMoreElements()) {
//			String name=em.nextElement().toString();
//			String value=session.getAttribute(name).toString();
//			out.println(
//			    	"<tr>"+
//			    		"<td>"+name+"</td><td>"+value+"</td><td>操作</td>"+
//			    	"</tr>");
//		}
		
		try {
			PreparedStatement sta;
			ResultSet rs;
			//session中有ID
			while(em.hasMoreElements()){
				//获取ID
				String key=em.nextElement().toString();
				//ID的对应的次数
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
