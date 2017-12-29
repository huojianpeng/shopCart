package cn.et.shop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.et.shop.utils.DBUtils;

public class QueryGoodServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		String goodName = request.getParameter("goodName");
		if (goodName==null) {
			goodName="";
		}
		PrintWriter out = response.getWriter();
		out.println("<style type=\"text/css\">table{"
				  + "text-align:center;width:100%;border: 1px solid black	" +
                  "}" + "th,td{" + "border: 1px solid black" + "}</style>");
		out.println("<form>"
						+ "<input type=\"text\" name=\"goodName\" value=\""+goodName+"\"/><input type=\"submit\" value=\"搜索\"/>"
						+ "</form>");

		out.println("<table style=\"width:100%;border: 1px solid black\">"
						+ "<tr>"
						+ "<th>显示图片</th><th>显示名称</th><th>商品单价</th><th>商品配置</th><th>操作</th>"
						+ "</tr>");

		
		
		// 查询逻辑
		String sql = "select * from GOODS where name like '%"+goodName+"%'";
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String imagePath = rs.getString("IMAGEPATH");
				double price = rs.getDouble("PRICE");
				String stock = rs.getString("STOCK");
				out.println("<tr>" + "<td><img src=\""
						+ request.getContextPath() + imagePath
						+ "\"/></td><td>" + name + "</td><td>" + price
						+ "</td><td>" + stock + "</td><td><input type='button' value='加入购物车' onclick=\"window.location='cart?add="+id+"';\"></td>" + "</tr>");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.println("</table>");

		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	private Connection conn = null;

	@Override
	public void destroy() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		try {
			conn = DBUtils.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
