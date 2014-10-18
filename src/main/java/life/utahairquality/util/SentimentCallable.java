package life.utahairquality.util;

import life.utahairquality.model.Result;
import life.utahairquality.service.SentimentService;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author cbrown
 */
public class SentimentCallable implements Callable {
    private final SentimentService service = new SentimentService();
    private List<Result> results = null;
    private int start = 0;
    private int size = 0;
    
    public SentimentCallable(List<Result> results, int start, int size) {
        this.results = results;
        this.start = start;
        this.size = size;
    }
    
    @Override
    public Object call() throws Exception {
        for (int i = start; i< start + size; i++) {
            Result result = results.get(i);
//            System.out.println("result=" + i);
            result.setSentiment(service.findSentiment(result.getText()));
        }
    
        return new Object();
    }
}
