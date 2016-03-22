import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.List;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws Exception {
        long startTime=System.currentTimeMillis();
        String text = "卢丝们挑战中共创辉煌";
        
        args = new String[] {"-props", "edu/stanford/nlp/hcoref/properties/zh-coref-default.properties" };
        Annotation document = new Annotation(text);
        Properties props = StringUtils.argsToProperties(args);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(document);
     
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
        System.out.println("word\tpos\tlemma\tner");
       
          for(CoreMap sentence: sentences) {
        	  
                for (CoreLabel token: sentence.get(TokensAnnotation.class)) {

                    String word = token.get(TextAnnotation.class);
                    String pos = token.get(PartOfSpeechAnnotation.class);

                    String ne = token.get(NamedEntityTagAnnotation.class);
                    String lemma = token.get(LemmaAnnotation.class);
                    
                    System.out.println(word+"\t"+pos+"\t"+lemma+"\t"+ne);

                }

          }
          
}
}