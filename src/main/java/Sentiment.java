import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;

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

public class Sentiment {
    public static void main(String[] args) throws Exception {

        String text = "掌上生活闪退";
        int result = getScore(text);
        System.out.println(result);

        text = "掌上生活很好";
        result = getScore(text);
        System.out.println(result);

        text = "掌上生活一般般";
        result = getScore(text);
        System.out.println(result);
    }

    public static int getScore(String text) throws IOException {
        int result = 0;
        String[] sentences = getSentences(text);
        for (String sentence : sentences) {
            result = result + score(sentence);
        }
        return result;
    }

    public static String[] getSentences(String text) {
        return text.trim().split("[\uff0c|\u3002|\uff1b]");
    }

    public static int score(String sentence) throws IOException {
        int opposite = 1;
        int wordDegree = 2;
        int score = 0;
        if ("".equals(sentence)) {
            return 0;
        }
        String[] words = new String[140];
        int index = 0;
        String[] args = new String[]{"-props", "edu/stanford/nlp/hcoref/properties/zh-coref-default.properties"};
        Annotation document = new Annotation(sentence);
        Properties props = StringUtils.argsToProperties(args);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence2 : sentences) {
            for (CoreLabel token : sentence2.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                words[index] = word;
                index++;
            }
        }
        for (int i = 0; i < index + 1; i++) {
            if (isSentiment(words[i]) == 1 || isSentiment(words[i]) == -1) {
                for (int j = 0; j < i; j++) {
                    if (isDegree(words[j])) {
                        wordDegree = Degree(words[j]);

                    }
                    if (isOpposite(words[j])) {
                        opposite = opposite * (-1);

                    }
                }
                 /*if(indexOfDegree>indexOfOpposite){
	    			 wordDegree = wordDegree/4;
	    			 opposite = opposite*(-1);
	    		 }*/
                score = score + sentimentScore(isSentiment(words[i]), wordDegree, opposite);
                wordDegree = 1;
                opposite = 1;

            }
        }
        return score;
    }

    public static String[] getPositveWords() throws IOException {
        String[] array = new String[5000];
        int i = 0;
        File file = FileUtils.getFileFromResources("positive.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        while ((str = br.readLine()) != null) {
            array[i] = str;
            i++;
        }
        br.close();
        String[] result = new String[i];
        System.arraycopy(array, 0, result, 0, i);
        return result;
    }

    public static String[] getNagtiveWords() throws IOException {
        String[] array = new String[9000];
        int i = 0;
        File file = FileUtils.getFileFromResources("negative.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        while ((str = br.readLine()) != null) {
            array[i] = str;
            i++;
        }
        br.close();
        String[] result = new String[i];
        System.arraycopy(array, 0, result, 0, i);
        return result;
    }

    public static String[] getOppositeWords() throws IOException {
        String[] array = new String[9000];
        int i = 0;
        File file = FileUtils.getFileFromResources("opposite.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        while ((str = br.readLine()) != null) {
            array[i] = str;
            i++;
        }
        br.close();
        String[] result = new String[i];
        System.arraycopy(array, 0, result, 0, i);
        return result;
    }

    public static String[] getDegree() throws IOException {
        String[] array = new String[9000];
        int i = 0;
        File file = FileUtils.getFileFromResources("degree3.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        while ((str = br.readLine()) != null) {
            array[i] = str;
            i++;
        }
        br.close();
        String[] result = new String[i];
        System.arraycopy(array, 0, result, 0, 219);
        return result;
    }

    public static int isSentiment(String word) throws IOException {
        if (word == null) {
            return 0;
        }
        int result = 0;
        String[] words = getPositveWords();
        String[] words2 = getNagtiveWords();
        for (int i = 0; i < words.length; i++) {
            if (words[i].contains(word)) {
                result = 1;
            } else if (words2[i].equals(word)) {
                result = -1;
            }
        }
        return result;
    }

    public static boolean isDegree(String word) throws IOException {
        boolean result = false;
        String[] words = getDegree();
        for (int i = 0; i < 219; i++) {
            if (words[i].equals(word)) {
                result = true;

            }
        }
        return result;
    }

    public static boolean isOpposite(String word) throws IOException {
        boolean result = false;
        String[] words = getOppositeWords();
        for (String word1 : words) {
            if (word1.equals(word)) {
                result = true;

            }
        }
        return result;

    }

    public static int Degree(String word) throws IOException {
        int result = 2;
        int index = 0;
        String[] words = getDegree();
        for (int i = 0; i < 219; i++) {
            if (words[i].equals(word)) {
                index = i;
            }
        }
        if (index < 111) {
            result = 4;
        }
        if (index >= 111 && index < 189) {
            result = 1;
        }
        if (index >= 189) {
            result = 4;
        }
        return result;
    }

    public static int sentimentScore(int sentiment, int wordDegree, int opposite) {
        int result = 0;
//        System.out.println(Sentiment + " " + wordDegree + " " + opposite);
        result = sentiment * wordDegree * opposite;
        return result;
    }
}
