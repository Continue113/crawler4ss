package org.sousai.crawler4ss.examples.qqrecords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRecords {
	public static void main(String[] args) {
		File file = new File("/home/lei/Desktop/data/1.txt");
        BufferedReader reader = null;
        ArrayList<StringBuffer> lists = new ArrayList<StringBuffer>() ;
        StringBuffer stringBuffer = new StringBuffer() ;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
             	if(datePattern(tempString)){
             		if(containsMatch(stringBuffer))
             			lists.add(stringBuffer) ;
             		stringBuffer = new StringBuffer() ;
            		continue ;
            	}
             	stringBuffer.append(tempString+"\n") ;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            System.out.println(lists);
        }
	}
	
	public static boolean datePattern(String date){
		//日期正则表达式 如1992/2/24 23:24:34
		Pattern p = Pattern.compile("(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2})", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE); 
        Matcher matcher = p.matcher(date); 
		
        return matcher.find() ;
	}
	
	public static boolean containsMatch(StringBuffer sb){
		String str = sb.toString() ;
		CharSequence timeChars = "比赛时间" ;
		CharSequence addressChars = "比赛地点" ;
		return str.contains(timeChars)&&str.contains(addressChars) ;
	}
}
