package life.utahairquality.util;

import life.utahairquality.enums.Wildcard;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import life.utahairquality.model.BuilderModel;
import java.util.List;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static life.utahairquality.model.BuilderModel.BooleanType.*;
import static life.utahairquality.model.BuilderModel.QueryType.*;
import org.elasticsearch.index.query.DisMaxQueryBuilder;

/**
 *
 * @author cbrown
 */
public class QueryUtil {
    public static QueryBuilder getDisMaxQueryBuilder(List<QueryBuilder> builders) {
        DisMaxQueryBuilder disq = disMaxQuery();
        for (QueryBuilder model : builders) {
            disq.add(model);
        }
        return disq;
    }

    public static QueryBuilder getTermsQueryBuilder(String field, String[] terms) {
        return termsQuery(field, terms);
    }
    
    public static QueryBuilder getTermQueryBuilder(String field, String term) {
        return termQuery(field, term)
                .boost(1.0F);
    }
    
    public static QueryBuilder getIdsQueryBuilder(String field, List<String> ids) {
        return inQuery(field, ids);
    }
    
    public static QueryBuilder getTermQueryNoBoostBuilder(String field, String term) {
        return termQuery(field, term);
    }
    
    public static QueryBuilder getBoostedTermQueryBuilder(String field, String term, float boost) {
        return termQuery(field, term)
                .boost(boost);
    }    
    
    public static QueryBuilder getGtRangeQuery(String field, String value) {
        return rangeQuery(field)
                .gte(value);
    }
    public static QueryBuilder getMatchAllQuery() {
        return matchAllQuery();
    }

    public static FilterBuilder getMatchAllFilter() {
        return matchAllFilter();
    }

    public static QueryBuilder getFilteredQuery(QueryBuilder qb1, FilterBuilder fb1) {
        return filteredQuery(
            qb1, 
            fb1
            );
    }

    public static QueryBuilder getBooleanShouldQuery(List<BuilderModel> models) {
        if (models.size() == 1) {
            return models.get(0).getQueryBuilder();
        }
        
        BoolQueryBuilder bq = boolQuery();
        for (BuilderModel model : models) {
            bq.should(model.getQueryBuilder());
        }
        return bq;
    }

    public static QueryBuilder getBooleanQuery(List<BuilderModel> models) {
        BoolQueryBuilder bq = boolQuery();
        for (BuilderModel model : models) {
            if (model.getBooleanType() == MUST) {
                bq.must(model.getQueryBuilder());
            } else if (model.getBooleanType() == SHOULD) {
                bq.should(model.getQueryBuilder());
            } else {
                bq.mustNot(model.getQueryBuilder());
            }
        }
        return bq;
    }

    public static QueryBuilder getBooleanShouldQuery(QueryBuilder... builders) {
        BoolQueryBuilder bq = boolQuery();
        for (QueryBuilder builder : builders) {
            if (builder != null) {
                bq.should(builder);
            }
        }
        return bq;
    }
    
    public static QueryBuilder getBooleanMustQuery(QueryBuilder... builders) {
        BoolQueryBuilder bq = boolQuery();
        for (QueryBuilder builder : builders) {
            if (builder != null) {
                bq.must(builder);
            }
        }
        return bq;
    }
    
    public static FilterBuilder getBooleanFilter(List<BuilderModel> models) {
        BoolFilterBuilder bq = boolFilter();
        for (BuilderModel model : models) {
            if (model.getBooleanType() == MUST) {
                bq.must(model.getFilterBuilder());
            } else if (model.getBooleanType() == SHOULD) {
                bq.should(model.getFilterBuilder());
            } else {
                bq.mustNot(model.getFilterBuilder());
            }
        }
        return bq;
    }

    public static QueryBuilder getQueryBuilder(String field, String term) {
        Wildcard wild = ParseUtil.isWildcard(term);
        if (wild == Wildcard.NONE) {
            //System.out.println("Query Wildcard.NONE " + term);
            return termQuery(field, term);
        } else if (wild == Wildcard.PREFIX) {
            //System.out.println("Query Wildcard.PREFIX " + term.substring(0, term.length() -1));
            return prefixQuery(field, term.substring(0, term.length() -1));
        } else {
            //System.out.println("Query Wildcard.FULL " + term);
            return wildcardQuery(field, term);
        }       
    }
    
    public static FilterBuilder getFilterBuilder(String field, String term) {
        Wildcard wild = ParseUtil.isWildcard(term);
        if (wild == Wildcard.NONE) {
            //System.out.println("Filter Wildcard.NONE " + term);
            return termFilter(field, term);
        } else if (wild == Wildcard.PREFIX) {
            //System.out.println("Filter Wildcard.PREFIX " + term.substring(0, term.length() -1));
            return prefixFilter(field, term.substring(0, term.length() -1));
        } else {
            // no wildcard filter
            //System.out.println("Filter Wildcard.FULL " + term);
            return null;
        }       
    }    

    // TODO: range filters are also available
    public static QueryBuilder getRangeQueryBuilder(String field, String from, String to) {
        if (from == null && to == null) {
            return null;
        }
        
        // Range <
        if (from == null) {
            return rangeQuery(field)
                .lt(to);
        }
        
        // Range >
        if (to == null) {
            return rangeQuery(field)
                .gt(from);
        }

        // Range inclusive
        return rangeQuery(field)
            .from(from)
            .to(to);
    }
    
    // TODO: range filters are also available
    public static FilterBuilder getRangeFilterBuilder(String field, String from, String to) {
        if (from == null && to == null) {
            return null;
        }
        
        // Range <
        if (from == null) {
            return rangeFilter(field)
                .lt(to);
        }
        
        // Range >
        if (to == null) {
            return rangeFilter(field)
                .gt(from);
        }

        // Range inclusive
        return rangeFilter(field)
            .from(from)
            .to(to);
    }
    
    public static QueryBuilder getTextQuery(String field, String terms) {
        return matchQuery(field, terms)
                .slop(1)
                .analyzer("simple");                
    }
    
    /**
     * Split on whitespace, and add all names to builder
     * @param builders
     * @param terms
     * @param field 
     */
    public static void addAllTerms(List<BuilderModel> builders, String terms, String field) {
        if (terms == null || terms.trim().length() == 0) { return; }
        
        // Check if we can split the name into components
        String[] splitTerms = terms.split("\\s");
        if (splitTerms.length > 1) {
            for (String splitTerm : splitTerms) {
                String term = ParseUtil.cleanTerm(splitTerm);
                if (term.isEmpty() || term.startsWith("@") || term.startsWith("#")) { continue; }
                if (term.startsWith("+")) {
                    builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1)), QUERY, SHOULD));
                } else if (term.startsWith("-")){
                    builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1)), QUERY, MUST_NOT));
                } else {
                    builders.add(new BuilderModel(getQueryBuilder(field, term), QUERY, MUST));
                }
            }
        } else {
            // No split, just add
            String term = ParseUtil.cleanTerm(terms);
            if (term.startsWith("+")) {                    
                builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1)), QUERY, SHOULD));
            } else if (term.startsWith("-")){
                builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1)), QUERY, MUST_NOT));
            } else {
                builders.add(new BuilderModel(getQueryBuilder(field, term), QUERY, MUST));
            }
        }
    }
    
    /**
     * Split on whitespace, and add all names to builder
     * @param builders
     * @param terms
     * @param field 
     */
    public static void addAllUsernames(List<BuilderModel> builders, String terms, String field) {
        // Check if we can split the name into components
        String[] splitTerms = terms.split("\\s");
        if (splitTerms.length > 1) {
            for (String term : splitTerms) {
                if (term.startsWith("@") || term.startsWith("#")) {
                    System.out.println("adding " + term);
                    builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1).toLowerCase()), QUERY, MUST));
                }
            }
        } else {
            // No split, just add
            String term = terms;
            if (term.startsWith("@") || term.startsWith("#")) {
                System.out.println("adding " + term);
                builders.add(new BuilderModel(getQueryBuilder(field, term.substring(1).toLowerCase()), QUERY, MUST));
            }
        }
    }

    /**
     * Split on whitespace, and add all names to builder
     * @param builders
     * @param terms
     * @param field 
     */
    public static void addAllTermsAsFilters(List<BuilderModel> builders, String terms, String field) {
        // Check if we can split the name into components
        String[] splitTerms = terms.split("\\s");
        for (String splitTerm : splitTerms) {
            String term = ParseUtil.cleanTerm(splitTerm);
            if (term.isEmpty()) { continue; }
            if (term.startsWith("+")) {                    
                builders.add(new BuilderModel(getFilterBuilder(field, term.substring(1)), FILTER, SHOULD));
            } else if (term.startsWith("-")){
                builders.add(new BuilderModel(getFilterBuilder(field, term.substring(1)), FILTER, MUST_NOT));
            } else {
                builders.add(new BuilderModel(getFilterBuilder(field, term), FILTER, MUST));
            }
        }
    }
}
