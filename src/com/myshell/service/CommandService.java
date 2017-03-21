package com.myshell.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.myshell.util.PathStack;

public class CommandService {
	String command;
	String currentPath;
	public CommandService(String command,String currentPath){
		this.command=command;
		this.currentPath=currentPath;
	}
	public String updateFileContent(File file,String content){
		String[] fileContent=content.split("\r\n");
		try {
			FileOutputStream fos=new FileOutputStream(file);
			OutputStreamWriter osw=new OutputStreamWriter(fos);
			BufferedWriter bw=new BufferedWriter(osw);
			for(int i=0;i<fileContent.length;i++){
				bw.write(fileContent[i]);				
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return "success";
	}
	public String[] getFileContent(File file){
		String[] fileContent=null;
		try {
			FileInputStream fis=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			int count=0;
			while((line=br.readLine())!=null){
				++count;
			}
			br.close();
			isr.close();
			fis.close();
			fileContent=new String[count];
			
			FileInputStream fis2=new FileInputStream(file);
			InputStreamReader isr2=new InputStreamReader(fis2);
			BufferedReader br2=new BufferedReader(isr2);
			String line2=null;
			int count2=0;
			while((line2=br2.readLine())!=null){
				fileContent[count2++]=line2;
			}
			br2.close();
			isr2.close();
			fis2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
		return fileContent;
	}
	public String getPath(PathStack stack2){
		PathStack stack=stack2;
		String cpath="";
		for(;!stack.isEmpty();){
			cpath=stack.pop()+cpath;
		}
		if(cpath.equals("")){
			cpath="~";
		}else{
			cpath=cpath.trim();
			String[] pathBack=cpath.split("/");			
			for(int i=1;i<pathBack.length;i++){		
				stack.push("/"+pathBack[i]);
			}
		}		
		return cpath;
	}
	public Boolean created(){
		String[] commandParams=command.split(" ");
		if(commandParams.length!=2||command.contains(".")){
			return false;
		}else{		
			File file=new File(currentPath+File.separator+commandParams[1]);
			if(!file.exists()){
				file.mkdir();
			}
		}
		return true;
	}
	public Boolean createf() throws IOException{
		String[] commandParams=command.split(" ");
		if(commandParams.length!=2){
			return false;
		}else{
			File file=new File(currentPath+File.separator+commandParams[1]);
			if(!file.exists()){
				file.createNewFile();
			}
		}
		return true;
	}
	public Boolean deleted(){
		String[] commandParams=command.split(" ");
		if(commandParams.length!=2||command.contains(".")){
			return false;
		}else{
			File file=new File(currentPath+File.separator+commandParams[1]);
			if(file.exists()&&file.isDirectory()){
				file.delete();
			}else{
				return false;
			}
		}
		return true;
	}
	public Boolean deletef(){
		String[] commandParams=command.split(" ");
		if(commandParams.length!=2){
			return false;
		}else{
			File file=new File(currentPath+File.separator+commandParams[1]);
			if(file.exists()&&!file.isDirectory()){
				file.delete();
			}else{
				return false;
			}
		}
		return true;
	}
	public Boolean changed(){
		String[] commandParams=command.split(" ");
		if(commandParams.length!=3||command.contains(".")){
			return false;
		}else{
			File file=new File(currentPath+File.separator+commandParams[1]);
			File file2=new File(currentPath+File.separator+commandParams[2]);
			if(file.exists()&&file.isDirectory()&&!file2.exists()){				
				file.renameTo(file2);
			}else{
				return false;
			}
		}
		return true;
	}
	public Boolean changef(){
		String[] commandParams=command.split(" ");
		if(commandParams.length!=3){
			return false;
		}else{
			File file=new File(currentPath+File.separator+commandParams[1]);
			File file2=new File(currentPath+File.separator+commandParams[2]);
			if(file.exists()&&!file.isDirectory()&&!file2.exists()){
				file.renameTo(file2);
			}else{
				return false;
			}
		}
		return true;
	}
	public String[] showd(){
		File f=new File(currentPath);
		File[] files=f.listFiles();
		String[] fileNames=null;
		if(files!=null){
			fileNames=new String[files.length+1];
			int maxlength=0;
			for(int j=0;j<files.length;j++){
				int size=(int) (files[j].length()/1024);
				int length=getlength(size);
				if(maxlength<length){
					maxlength=length;
				}
			}
			for(int i=0;i<files.length;i++){	
				 long modifiedTime = files[i].lastModified();
                 Date date=new Date(modifiedTime);
                 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String dd=sdf.format(date);
                 String fileInfo=property(files[i]);
                 int size=(int)files[i].length()/1024;
                 int length=getlength(size);
                 for(int k=0;k<maxlength-length+1;k++){
                	 fileInfo=fileInfo+"&nbsp;&nbsp;";       
                 }
                 fileInfo+=size+"KB"+"&nbsp;&nbsp;"+dd+"&nbsp;&nbsp;"+files[i].getName();
				 fileNames[i+1]=fileInfo;					
			}
		}		
		return fileNames;
	}
	public int getlength(int size){
		if(size==0){
			return 1;
		}
		int length=0;
		while(size!=0){
			++length;
			size/=10;
		}
		return length;
	}
	public String property(File file){
		String info="";
		if(file.isDirectory()){
			info+="d";
		}else{
			info+="-";
		}
		if(file.canRead()){
			info+="r";
		}else{
			info+="-";
		}
		if(file.canWrite()){
			info+="w";
		}else{
			info+="-";
		}
		if(file.canExecute()){
			info+="x";
		}else{
			info+="-";
		}
		return info;
	}

}
