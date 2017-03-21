package com.myshell.util;

import java.util.Stack;

public class PathStack {
	Stack<String> stack;
	public PathStack(){
		stack=new Stack<String>();
	}
	public void push(String path){
		stack.push(path);
	}
	public String pop(){
		return stack.pop();
	}
	public boolean isEmpty(){
		return stack.empty();	
	}
	public void clearAll(){
		stack.clear();
	}
}
