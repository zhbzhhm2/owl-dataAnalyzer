import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

// -------------------------------------------------------------------------------------
// CMB Confidential
//
// Copyright (C) 2016Äê2ÔÂ29ÈÕ China Merchants Bank Co., Ltd. All rights reserved.
//
// No part of this file may be reproduced or transmitted in any form or by any
// means,
// electronic, mechanical, photocopying, recording, or otherwise, without prior
// written permission of China Merchants Bank Co., Ltd.
//
// -------------------------------------------------------------------------------------
import com.cmbchina.ccd.pluto.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
public class testGet {
	public static void main(String[] args) throws IOException{
		File a1 = new File("resources/xinlangweibo.txt");
		Scanner input = new Scanner(a1);
		input.useDelimiter("}");
		while(input.hasNext()){
			String ips = input.next();
			//String gbkStr = new String(ips.getBytes("ISO8859-1"), "GBK");
			String gbkStr = decodeUnicode(ips);
			gbkStr = ips+"}";
			gbkStr.replace(" ", "");
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap = JsonUtils.stringToMap(gbkStr);
			//String contents = JsonUtils.objectToJsonString(gbkStr);
			//jsonMap = JsonUtils.stringToMap(contents);
			String text = jsonMap.get("content");
			jsonMap = JsonUtils.stringToMap(gbkStr);
			System.out.println(gbkStr);
			System.out.println(text);
			//int result = sentiment.getScore(text);
			//System.out.println(result);
		}
			//int result = sentiment.getScore(gbkStr);
			//System.out.println(result); 
	}
	
	
	public static String decodeUnicode(String theString) {    
		  
	     char aChar;    
	  
	      int len = theString.length();    
	  
	     StringBuffer outBuffer = new StringBuffer(len);    
	  
	     for (int x = 0; x < len;) {    
	  
	      aChar = theString.charAt(x++);    
	  
	      if (aChar == '\\') {    
	  
	       aChar = theString.charAt(x++);    
	  
	       if (aChar == 'u') {    
	  
	        // Read the xxxx    
	  
	        int value = 0;    
	  
	        for (int i = 0; i < 4; i++) {    
	  
	         aChar = theString.charAt(x++);    
	  
	         switch (aChar) {    
	  
	         case '0':    
	  
	         case '1':    
	  
	         case '2':    
	  
	         case '3':    
	  
	        case '4':    
	  
	         case '5':    
	  
	          case '6':    
	           case '7':    
	           case '8':    
	           case '9':    
	            value = (value << 4) + aChar - '0';    
	            break;    
	           case 'a':    
	           case 'b':    
	           case 'c':    
	           case 'd':    
	           case 'e':    
	           case 'f':    
	            value = (value << 4) + 10 + aChar - 'a';    
	           break;    
	           case 'A':    
	           case 'B':    
	           case 'C':    
	           case 'D':    
	           case 'E':    
	           case 'F':    
	            value = (value << 4) + 10 + aChar - 'A';    
	            break;    
	           default:    
	            throw new IllegalArgumentException(    
	              "Malformed   \\uxxxx   encoding.");    
	           }    
	  
	         }    
	          outBuffer.append((char) value);    
	         } else {    
	          if (aChar == 't')    
	           aChar = '\t';    
	          else if (aChar == 'r')    
	           aChar = '\r';    
	  
	          else if (aChar == 'n')    
	  
	           aChar = '\n';    
	  
	          else if (aChar == 'f')    
	  
	           aChar = '\f';    
	  
	          outBuffer.append(aChar);    
	  
	         }    
	  
	        } else   
	  
	        outBuffer.append(aChar);    
	  
	       }    
	  
	       return outBuffer.toString();    
	  
	      }   
}
