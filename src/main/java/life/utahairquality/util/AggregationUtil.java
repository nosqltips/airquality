package life.utahairquality.util;

import life.utahairquality.enums.AggregationField;
import life.utahairquality.model.FacetRequest;
import life.utahairquality.model.SelectableFacet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Interval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;

/**
 *
 * @author cbrown
 */
public class AggregationUtil {
    private static final Logger logger = Logger.getLogger(AggregationUtil.class.getName());

    public static AbstractAggregationBuilder getTerms(String name, String field, Integer size) {
        if (size == null) { size = 10; }
        return terms(name)
                .field(field)
                .size(size);
    }

    public static AbstractAggregationBuilder getAverage(String name, String field) {
        return avg(name)
                .field(field);
    }

    public static AbstractAggregationBuilder getCount(String name, String field) {
        return count(name)
                .field(field);
    }

    public static AbstractAggregationBuilder getStats(String name, String field) {
        return stats(name)
                .field(field);
    }

    public static AbstractAggregationBuilder getSignificantTerms(String name, String field, Integer size) {
        if (size == null) { size = 10; }
        return significantTerms(name)
                .field(field)
                .size(size);
    }

    public static AbstractAggregationBuilder getPercentiles(String name, String field, double[] percentiles) {
        if (percentiles == null || percentiles.length == 0) {
            return percentiles(name)
                    .field(field);
        } else {
            return percentiles(name)
                    .field(field)
                    .percentiles(percentiles);
        }
    }

    public static AbstractAggregationBuilder getUnique(String name, String field) {
        return cardinality(name)
                .field(field);
    }

    public static AbstractAggregationBuilder getHistogram(String name, String field, long interval) {
        return histogram(name)
                .field(field)
                .interval(interval)
                .minDocCount(0);
    }

    public static AbstractAggregationBuilder getDateHistogram(String name, String field, Interval interval) {
        return dateHistogram(name)
                .field(field)
                .interval(interval)
                .minDocCount(0);
    }

    public static AbstractAggregationBuilder getNested(String name, String path, AbstractAggregationBuilder subAggregation) {
        return nested(name)
                .path(path)
                .subAggregation(subAggregation);
    }

    /**
     * Parse all of the aggregations and return as a list of FacetRequests.
     * 
     * @param aggregations
     * @return 
     */
    public static List<FacetRequest> parseAggregations(Aggregations aggregations) {
        List<FacetRequest> requests = new ArrayList<>();
        for (Aggregation aggregation: aggregations.asList()) {
            try {
                String aggName = aggregation.getName();
                AggregationField aggField = AggregationField.valueOf(aggName);

                List<SelectableFacet> selectables = parseSingleAggregation(aggregation, aggField);

                if (selectables != null) {
                    requests.add(new FacetRequest()
                        .setField(aggField)
                        .setSelectables(selectables)
                    );
                }
            } catch (Exception e) {                    
                logger.log(Level.WARNING, "Could not add aggregation for type {0}", aggregation.getName());
                e.printStackTrace();
            }
        }
        
        return ! requests.isEmpty() ? requests : null;
    }
    
    /**
     * Parse a single aggregation and return as a list of SelectableFacets.
     * 
     * @param aggregation
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseSingleAggregation(Aggregation aggregation, AggregationField aggField) {
        if (aggregation == null) {
            return null;
        }

        if (aggregation instanceof Terms) {
            return parseTerms((Terms) aggregation, aggField);
        } else if (aggregation instanceof SignificantTerms) {
            return parseSignificantTerms((SignificantTerms) aggregation, aggField);
        } else if (aggregation instanceof DateHistogram) {
            return parseDateHistogram((DateHistogram) aggregation, aggField);
        } else if (aggregation instanceof Histogram) {
            return parseHistogram((Histogram) aggregation, aggField);
        } else if (aggregation instanceof Range) {
            return parseRange((Range) aggregation, aggField);
        } else if (aggregation instanceof Global) {
            return parseGlobal((Global) aggregation, aggField);
        } else if (aggregation instanceof Avg) {
            return parseAvg((Avg) aggregation, aggField);
        } else if (aggregation instanceof Cardinality) {
            return parseCardinality((Cardinality) aggregation, aggField);
        } else if (aggregation instanceof Percentiles) {
            return parsePercentiles((Percentiles) aggregation, aggField);
        } else if (aggregation instanceof Stats) {
            return parseStats((Stats) aggregation, aggField);
        }

        return null;
    }

    /**
     * Parse all terms.
     * 
     * @param terms
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseTerms(Terms terms, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket: terms.getBuckets()) {
            selectables.add(
                new SelectableFacet()
                    .setName(bucket.getKey())
                    .setCount(bucket.getDocCount())
            );
        }

        return selectables;
    }

    /**
     * Parse all significant terms.
     * 
     * @param sigTerms
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseSignificantTerms(SignificantTerms sigTerms, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms.Bucket bucket: sigTerms.getBuckets()) {
            selectables.add(
                new SelectableFacet()
                    .setName(bucket.getKey())
                    .setCount(bucket.getDocCount())
            );
        }

        return selectables;
    }

    /**
     * Parse regular histogram.
     * 
     * @param histogram
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseHistogram(Histogram histogram, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket bucket: histogram.getBuckets()) {
            selectables.add(
                new SelectableFacet()
                    .setName(bucket.getKey())
                    .setCount(bucket.getDocCount())
            );
        }

        return selectables;
    }

    /**
     * Parse date histogram,
     * 
     * @param dateHistogram
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseDateHistogram(DateHistogram dateHistogram, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram.Bucket bucket: dateHistogram.getBuckets()) {
            selectables.add(
                new SelectableFacet()
                    .setName(bucket.getKey())
                    .setTime(bucket.getKeyAsDate().getMillis())
                    .setCount(bucket.getDocCount())
            );
        }

        return selectables;
    }

    /**
     * Parse range.
     * 
     * @param range
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseRange(Range range, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (org.elasticsearch.search.aggregations.bucket.range.Range.Bucket bucket: range.getBuckets()) {
            selectables.add(
                new SelectableFacet()
                    .setName(bucket.getFrom() + "-" + bucket.getTo())
                    .setCount(bucket.getDocCount())
            );
        }

        return selectables;
    }

    /**
     * Parse global.
     * 
     * @param global
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseGlobal(Global global, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        selectables.add(
            new SelectableFacet()
                .setName(global.getName())
                .setCount(global.getDocCount())
        );

        return selectables;
    }

    /**
     * Parse average.
     * 
     * @param avg
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseAvg(Avg avg, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        selectables.add(
            new SelectableFacet()
                .setName(avg.getName())
                .setValue(avg.getValue())
        );

        return selectables;
    }

    /**
     * Parse cardinality (number of unique terms)
     * 
     * @param cardinality
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseCardinality(Cardinality cardinality, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        selectables.add(
            new SelectableFacet()
                .setName(cardinality.getName())
                .setCount(cardinality.getValue())
        );

        return selectables;
    }

    /**
     * Parse percentiles.
     * 
     * @param percentiles
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parsePercentiles(Percentiles percentiles, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        for (Percentile percentile: percentiles) {
            selectables.add(
                new SelectableFacet()
                    .setName(Double.toString(percentile.getPercent()))
                    .setValue(percentile.getValue())
            );
        }

        return selectables;
    }

    /**
     * Parse stats (count, avg, sum, min, max)
     * 
     * @param stats
     * @param aggField
     * @return 
     */
    public static List<SelectableFacet> parseStats(Stats stats, AggregationField aggField) {
        List<SelectableFacet> selectables = new ArrayList<>();
        selectables.add(
            new SelectableFacet()
                .setName(stats.getName())
                .setCount(stats.getCount())
        );

        selectables.add(
            new SelectableFacet()
                .setName("avg")
                .setValue(stats.getAvg())
        );

        selectables.add(
            new SelectableFacet()
                .setName("sum")
                .setValue(stats.getSum())
        );

        selectables.add(
            new SelectableFacet()
                .setName("min")
                .setValue(stats.getMin())
        );

        selectables.add(
            new SelectableFacet()
                .setName("max")
                .setValue(stats.getMax())
        );

        return selectables.isEmpty() ? null : selectables;
    }
}
