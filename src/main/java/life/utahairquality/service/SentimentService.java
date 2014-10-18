package life.utahairquality.service;

import life.utahairquality.util.RegexUtil;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.Properties;

/**
 *
 * @author cbrown
 */
public class SentimentService {
    private final Properties props = new Properties();
    private final StanfordCoreNLP pipeline;

    public SentimentService () {
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }
    
    public Integer findSentiment(String line) {
        // 2 is neutral
        int mainSentiment = 2;

        if (line != null && line.length() > 0) {
            // Remove all of the garbage from the tweet before attempting sentiment.
            line = RegexUtil.removeGarbage(line);
            if (line.length() == 0) {
                return mainSentiment;
            }
        
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
 
        // 5-point scale of 0 = very negative, 1 = negative, 2 = neutral, 3 = positive, and 4 = very positive.
//        if (mainSentiment > 4 || mainSentiment < 0) {
//            return null;
//        }

        return mainSentiment;
    }
}
