import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

// -------------------------------------------------------------------------------------
// CMB Confidential
//
// Copyright (C) 2016年2月4日 China Merchants Bank Co., Ltd. All rights reserved.
//
// No part of this file may be reproduced or transmitted in any form or by any
// means,
// electronic, mechanical, photocopying, recording, or otherwise, without prior
// written permission of China Merchants Bank Co., Ltd.
//
// -------------------------------------------------------------------------------------

public class sentiment {
	 public static void main(String[] args) throws Exception {
		String text = "掌上生活老是闪退,我再也不想用它了";
		
		 int result = getScore(text);
		 System.out.println(result);
	 }
	 
	 public static int getScore(String text) throws IOException {
		 int result = 0;
		 String[] sentences = getSentences(text);
		 for(int i=0;i<sentences.length;i++) {
			 //System.out.println(sentences[i]);
			 result = result+score(sentences[i]);
		 }
		 return result;
	 }
	 
	 public static String[] getSentences(String text) {
		 String[] result = new String[10];
		 int i = 0;
		 int j = 0;
		 for (int k=0;k<text.length();k++) {
			 if (text.charAt(k)==','||text.charAt(k)=='.'||text.charAt(k)==';')	{
				result[j] = text.substring(i,k);
				System.out.println(result[j]);
				j++;
				i = k+1;
			 }
		 }
		 String[] result2 = new String[j];
		 for(int k=0;k<j;k++){
			 result2[k] = result[k];
		 }
		 return result2;
	 }
	 
	 public static int score(String sentence) throws IOException {
		 int opposite = 1;
		 int wordDegree = 2;
		 int score = 0;
		 if ("".equals(sentence)){
			 return 0;
		 }
		 String[] words = new String[140]; 
		 int index = 0;
		 String[] args = new String[] {"-props", "edu/stanford/nlp/hcoref/properties/zh-coref-default.properties" };
		 Annotation document = new Annotation(sentence);
		 Properties props = StringUtils.argsToProperties(args);
	     StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	     pipeline.annotate(document);
	     List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	     for(CoreMap sentence2: sentences) {
	    	 for (CoreLabel token: sentence2.get(TokensAnnotation.class)) {
	    		 String word = token.get(TextAnnotation.class);
	    		 words[index] = word;
	    		 index++;
             }
	     }
	     for (int i=0;i<index+1;i++){
	    	 if (isSentiment(words[i])==1||isSentiment(words[i])==-1){
	    		 for (int j=0;j<i;j++){
	    			 if(isDegree(words[j])){
	    				 wordDegree = Degree(words[j]);
	    			
	    			 }
	    			 if(isOpposite(words[j])){
	    				 opposite = opposite*(-1);
	    
	    			 }
	    		 }
	    		 /*if(indexOfDegree>indexOfOpposite){
	    			 wordDegree = wordDegree/4;
	    			 opposite = opposite*(-1);
	    		 }*/
	    		 score = score+sentimentScore(isSentiment(words[i]),wordDegree,opposite);
	    		 wordDegree = 1;
	    		 opposite = 1;

	    	 }
	     }
		 return score;
	 }
	 
	 public static String[] getPositveWords() throws IOException{
		 String[] array = new String[5000];
		 int i = 0;
		 File file = new File("resources/positive.txt");
		 InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
		 BufferedReader br = new BufferedReader(isr);
	     String str = null;
	     while((str = br.readLine()) != null) {
	    	 array[i] = str;
	    	 i++;
	     }
	     br.close();
	     String[] result = new String[i];
	     for(int j=0;j<i;j++){
	    	 result[j] = array[j];
	     }
	     return result;
	}
	 
	 public static String[] getNagtiveWords() throws IOException{
		 String[] array = new String[9000];
		 int i = 0;
		 File file = new File("resources/negative.txt");
		 InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
		 BufferedReader br = new BufferedReader(isr);
	     String str = null;
	     while((str = br.readLine()) != null) {
	    	 array[i] = str;
	    	 i++;
	     }
	     br.close();
	     String[] result = new String[i];
	     for(int j=0;j<i;j++){
	    	 result[j] = array[j];
	     }
	     return result;
	}
	
	 public static String[] getOppositeWords() throws IOException{
		 String[] array = new String[9000];
		 int i = 0;
		 File file = new File("resources/opposite.txt");
		 InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
		 BufferedReader br = new BufferedReader(isr);
	     String str = null;
	     while((str = br.readLine()) != null) {
	    	 array[i] = str;
	    	 i++;
	     }
	     br.close();
	     String[] result = new String[i];
	     for(int j=0;j<i;j++){
	    	 result[j] = array[j];
	     }
	     return result;
	 }
	 
	 public static String[] getDegree() throws IOException{
		 String[] array = new String[9000];
		 int i = 0;
		 File file = new File("resources/degree3.txt");
		 InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
		 BufferedReader br = new BufferedReader(isr);
	     String str = null;
	     while((str = br.readLine()) != null) {
	    	 array[i] = str;
	    	 i++;
	     }
	     br.close();
	     String[] result = new String[i];
	     for(int j=0;j<219;j++){
	    	 result[j] = array[j];
	     }
	     return result;
	 }
	 
	 public static int isSentiment(String word) throws IOException{
		 int result = 0;
		 String[] words = getPositveWords();
		 String[] words2 = getNagtiveWords();
		 for (int i=0;i<words.length;i++){
			 if(words[i].equals(word)){
				 result = 1;
			 }
			 else if(words2[i].equals(word)){
				 result = -1;
			 }
		 }
		 return result;
	 }
	 
	 public static boolean isDegree(String word) throws IOException{
		 boolean result = false;
		 String[] words = getDegree();
		 for (int i=0;i<219;i++){
			 if(words[i].equals(word)){
				 result = true;

			 }
		 }
		 return result;
	 }
	 
	 public static boolean isOpposite(String word) throws IOException{
		 boolean result = false;
		 String[] words = getOppositeWords();
		 for (int i=0;i<words.length;i++){
			 if(words[i].equals(word)){
				 result = true;

			 }
		 }
		 return result;

	 }
	 
	 public static int Degree(String word) throws IOException{
		 int result = 2;
		 int index = 0;
		 String[] words = getDegree();
		 for (int i=0;i<219;i++){
			 if(words[i].equals(word)){
				 index = i;
			 }
		 }
		 if (index < 111){
			 result = 4;
		 }
		 if (index >= 111 && index < 189){
			 result = 1;
		 }
		 if (index >= 189){
			 result = 4;
		 }
		 return result;
	 }
	 
	 public static int sentimentScore(int sentiment,int wordDegree,int opposite){
		 int result = 0;
		 System.out.println(sentiment+" "+wordDegree+" "+opposite);
		 result = sentiment*wordDegree*opposite;
		 return result;
	 }
}
