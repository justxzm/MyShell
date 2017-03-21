package com.myshell.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myshell.service.CommandService;
import com.myshell.util.DeleteSpace;
import com.myshell.util.PathStack;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class CommandServlet
 */
@WebServlet("/CommandServlet")
public class CommandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommandServlet() {
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
		String command =DeleteSpace.replace(request.getParameter("command"));
		String username=request.getSession().getAttribute("username").toString();
		String rootPath=request.getSession().getAttribute("rootPath").toString();
		String currentPath=request.getSession().getAttribute("currentPath").toString();
		String fileContentUpdate=request.getParameter("fileContent");	
		PathStack stack=(PathStack) request.getSession().getAttribute("pathStack");
		if(stack==null){
			stack=new PathStack();
			request.getSession().setAttribute("pathStack", stack);
		}
		PrintWriter out = response.getWriter();
		JSONObject jo = new JSONObject();
		String[] fileNames=null;
		String[] fileContents=null;
		CommandService commandService=new CommandService(command,currentPath);
		jo.put("path",commandService.getPath(stack));
		if(command.equals("showd")){
			fileNames=commandService.showd();			
			if(fileNames!=null){
				fileNames[0]="total "+(fileNames.length-1);
			}else{
				fileNames=new String[]{"total 0"};
			}
		}else if(command.contains("created")){
			if(!commandService.created()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("createf")){
			if(!commandService.createf()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("deleted")){
			if(!commandService.deleted()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("deletef")){
			if(!commandService.deletef()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("changed")){
			if(!commandService.changed()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("changef")){
			if(!commandService.changef()){
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("open")){
			String[] commandParams=command.split(" ");
			if(commandParams.length==2){
				File file=new File(currentPath+"/"+commandParams[1]);
				if(file.exists()&&!file.isDirectory()){
					fileContents=commandService.getFileContent(file);
					if(fileContents==null){	
						jo.put("error","命令错误或不存在");
					}else{											
						jo.put("fileContents", fileContents);
						request.getSession().setAttribute("openedFileName", commandParams[1]);
					}
				}else{
					jo.put("error","命令错误或不存在");
				}
			}else{
				jo.put("error","命令错误或不存在");
			}
		}else if(command.contains("close")){
			String[] commandParams=command.split(" ");
			String openedFileName=request.getSession().getAttribute("openedFileName").toString();
			if(commandParams.length==2&&openedFileName.equals(commandParams[1])){
				File file=new File(currentPath+"/"+commandParams[1]);
				if(file.exists()&&!file.isDirectory()){			
//					String str=commandService.updateFileContent(file,fileContentUpdate);
					String str="";
					if(str==null){	
						jo.put("error","命令错误或不存在");
					}else{
						request.getSession().setAttribute("openedFileName"," ");
					}
				}else{
					jo.put("error","命令错误或不存在");
				}
			}else{
				jo.put("error","命令错误或不存在");
			}
		}else if(command.equals("cd ~")){
			request.getSession().setAttribute("currentPath", rootPath);
			stack.clearAll();
			jo.put("path","~");
		}else if(command.contains("cd")){
			String[] commandParams=command.split(" ");
			if(commandParams.length==2&&commandParams[1].equals("..")){	
				if(!stack.isEmpty()){
					String relativePath=(String)stack.pop();
					int mark=currentPath.indexOf(relativePath);
					currentPath=currentPath.substring(0, mark);		
					if(stack.isEmpty()){
						jo.put("path","~");
					}else{
						jo.put("path",commandService.getPath(stack));
					}
				}else{
					jo.put("path","~");
				}
			}else if(commandParams.length==2&&!command.contains("/")){
				String testPath=currentPath+"/"+commandParams[1];
				File file=new File(testPath);
				if(file.exists()&&file.isDirectory()){
					stack.push("/"+commandParams[1]);
					currentPath=currentPath+"/"+commandParams[1];		
					jo.put("path",commandService.getPath(stack));
				}else{
					jo.put("error","命令错误或不存在");
				}
			}else{
				jo.put("error","命令错误或不存在");
			}
			request.getSession().setAttribute("currentPath", currentPath);		
		}else if(command.equals("")){
			
		}else{
			jo.put("error","命令错误或不存在");
		}
		if(((PathStack) request.getSession().getAttribute("pathStack")).isEmpty()){
			jo.put("path","~");
		}
		request.getSession().setAttribute("pathStack", stack);
		if(command.contains("open")){
			jo.put("message", "open");
		}else if(command.contains("close")){
			jo.put("message", "close");
		}else{
			jo.put("message", command);
		}
		jo.put("command", command);
		jo.put("fileNames", fileNames);
		out.write(jo.toString());
	}
}
