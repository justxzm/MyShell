<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.myshell.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
<meta charset="UTF-8">
<title>MyShell</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon"/>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script>
        $(document).ready(function() {
            $("#mainBody").hide(0);               
            $("#fileContents").hide(0);
            $("#myModal").blur(function() {
                $("#command").focus();
            });   
            $("#submitBtn").click(function(){
            	var content=$("#submitBtn").html();
            	if(content=='快速连接/注册(通用)'){        
            		var username=$.trim($("#username").val());
                	var password=$("#password").val();
                	if(username==''||password==''){
                		$("#error").html('用户/密码不能为空');
                	}else{
                		$("#error").html('');
                		$.ajax({
                			type:"post",
                			url:"/MyShell/loginServlet",
                			dataType:'json',
                			data:'username=' + username+"&password="+password,
                			success: function(json) {                        
                				if(json.message=='登陆成功'){
                					$("#username").val('');        
                					$("#password").val('');                 	
                					$("#error").html('');
                					$("#userInfo").html(json.username);               			
                					$("#mainBody").show(100);
                					$("#command").focus();
                					$("#submitBtn").html('断开连接');              
                				}else if(json.message=='用户存在，密码错误'){
                					$("#error").html('用户存在，密码错误');
                				}
                			}
                		}); 
                	}   
            	}else if(content=='断开连接'){     
            		<%request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("rootPath");
			request.getSession().removeAttribute("currentPath");
			request.getSession().removeAttribute("pathStack");
			request.getSession().removeAttribute("openedFileName");%>
           		 	location.reload();
            	}              	                         
            });
        });
        function submit(){
            var command= $.trim($("#command").val());    
            var userInfo=$("#userInfo").html();                 
            	$.ajax({
        			type:"post",
        			url:"/MyShell/commandServlet",
        			dataType:'json',
        			data:'command=' + command,
        			success: function(json) {   
        				$("#fileContents").text("");
    					$("#fileContents").hide(0);
        				$("#result").append("<tr><td style='word-break:break-all;'>"+userInfo+"@VM-226-6-ubuntu:"+json.path+"# &nbsp"+json.command+"</td></tr>");
        				$("#path").html(json.path);
        				if(json.error=='命令错误或不存在'){   					
        					$("#result").append("<tr><td style='word-break:break-all;'>命令错误或不存在</td></tr>");
        				}else{       			       		
        					if(json.message=='showd'){       				
            					var fileNames=json.fileNames;
            					for(i=0;i<fileNames.length;i++){
            						$("#result").append("<tr><td style='word-break:break-all;'>"+fileNames[i]+"</td></tr>");
            					}
            				}else if(json.message=='open'){
            					$("#fileContents").text("");           		
            					var fileContents=json.fileContents;         
            					for(i=0;i<fileContents.length;i++){
            						$("#result").append("<tr><td style='word-break:break-all;'>"+fileContents[i]+"</td></tr>");
            					}          					
            				}else if(json.message=='close'){
            					$("#fileContents").text("");
            					$("#fileContents").hide(0);
            				}
        				}    				
        			}
        		});           
            $("#command").val("");
        }
        </script>

<style>
body {
	background-color: black;
	color: white;
	margin-top: 66px;
	margin-left: 14px;
	font-size: large;
}

#userInfo {
	width: 100%;
}

#command {
	background-color: black;
	color: white;
	border: none;
	border-color: black;
	width: 100%;
	outline: none;
}

#mainBody {
	width: 100%;
}
</style>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">MyShell</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="#" id="utext">用户名(U):</a></li>
				<li><a><input type="text" id="username"></a></li>
				<li><a href="#" id="ptext">密码(W):</a></li>
				<li><a><input type="password" id="password"></a></li>
				<li><a><button id="submitBtn">快速连接/注册(通用)</button></a></li>
				<li><a id="error" href="#" style='color: red;'></a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#" data-toggle="modal" data-target="#myModal">帮助文档(H)</a></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid --> </nav>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" style="color: #000000;">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" onclick="javascript:$('#command').focus();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">API</h4>
				</div>
				<div class="modal-body">
					<table>
						<tr>
							<td>进入主目录</td>
							<td>cd ~</td>
						</tr>
						<tr>
							<td>返回上级目录</td>
							<td>cd ..</td>
						</tr>
						<tr>
							<td>进入指定目录</td>
							<td>cd [DirectoryName]</td>
						</tr>
						<tr>
							<td>创建目录</td>
							<td>created [DirectoryName]</td>
						</tr>
						<tr>
							<td>创建文件</td>
							<td>createf [FileName]</td>
						</tr>
						<tr>
							<td>转换目录</td>
							<td>changed [OldDirectoryName] [NewDirectoryName]</td>
						</tr>
						<tr>
							<td>转换文件</td>
							<td>changef [OldFileName] [NewFileName]</td>
						</tr>
						<tr>
							<td>删除目录</td>
							<td>deleted [DirectoryName]</td>
						</tr>
						<tr>
							<td>删除目录下的文件</td>
							<td>deletef [FileName]</td>
						</tr>
						<tr>
							<td>打开、关闭文件</td>
							<td>open [FileName] 、 close [FileName]</td>
						</tr>
						<tr>
							<td>显示目录内容</td>
							<td>showd</td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="javascript:$('#command').focus();">Close</button>
				</div>
			</div>
		</div>
	</div>


	<content id="mainBody">
	<table width="100%" border="0" id="result">
		<tr>
			<td width="100%" style='word-break: break-all;'>Welcome to
				Ubuntu 14.04.1 LTS (GNU/Linux 3.13.0-105-generic i686)</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td width="22px" style="white-space: nowrap"><span id="userInfo"></span>@VM-226-6-ubuntu:<span
				id="path">~</span># &nbsp</td>
			<td width="auto"><input id="command"
				onkeypress="if(event.keyCode==13) {submit();return false;}" />
		</tr>
	</table>
	</content>

</body>
</html>