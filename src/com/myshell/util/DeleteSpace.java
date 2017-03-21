package com.myshell.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DeleteSpace {
	public static String replace(String str){
		Pattern p = Pattern.compile("\\s+");      
        Matcher m = p.matcher(str);      
        str = m.replaceAll(" ");   
		return str;
	}
}
