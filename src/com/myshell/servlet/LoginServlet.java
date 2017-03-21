package com.myshell.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myshell.model.User;
import com.myshell.service.UserService;
import com.myshell.util.PathStack;

import net.sf.json.JSONObject;





/**
 * Servlet implementation class AjaxServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();
		JSONObject jo = new JSONObject();
		UserService userService=UserService.getInstance();
		User user=new User(username,password);
		if(userService.isExistName(user)){
			if(userService.isRightPasswd(user)){
				jo.put("message", "登陆成功");
				jo.put("username", username);
				request.getSession().setAttribute("username", username);
				String rootPath=getServletContext().getRealPath("/FileSystem").replace('\\', '/')+"/"+username;
				request.getSession().setAttribute("rootPath", rootPath);
				request.getSession().setAttribute("currentPath", rootPath);
				request.getSession().setAttribute("openedFileName"," ");
				PathStack stack=new PathStack();
				request.getSession().setAttribute("pathStack", stack);
			}else{
				jo.put("message", "用户存在，密码错误");
			}
		}else{
			userService.add(user);
			jo.put("message", "登陆成功");
			jo.put("username", username);
			request.getSession().setAttribute("username", username);
			String rootPath=getServletContext().getRealPath("/FileSystem").replace('\\', '/')+"/"+username;
			request.getSession().setAttribute("rootPath", rootPath);
			request.getSession().setAttribute("currentPath", rootPath);
			request.getSession().setAttribute("openedFileName"," ");
			PathStack stack=new PathStack();
			request.getSession().setAttribute("pathStack", stack);
			File file=new File(rootPath);
			if(!file.exists()){
				file.mkdir();
			}
		}
		out.write(jo.toString());
	}
}
