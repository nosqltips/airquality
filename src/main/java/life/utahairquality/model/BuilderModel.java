package life.utahairquality.model;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 *
 * @author cbrown
 */
public class BuilderModel {
    // TODO: Use a plain Builder here?
    private QueryBuilder queryBuilder;
    private FilterBuilder filterBuilder;
    private QueryType queryType;
    private BooleanType booleanType;

    public BuilderModel(QueryBuilder queryBuilder, QueryType queryType, BooleanType booleanType) {
        this.queryBuilder = queryBuilder;
        this.queryType = queryType;
        this.booleanType = booleanType;
    }

    public BuilderModel(FilterBuilder filterBuilder, QueryType queryType, BooleanType booleanType) {
        this.filterBuilder = filterBuilder;
        this.queryType = queryType;
        this.booleanType = booleanType;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public FilterBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public void setFilterBuilder(FilterBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public BooleanType getBooleanType() {
        return booleanType;
    }

    public void setBooleanType(BooleanType booleanType) {
        this.booleanType = booleanType;
    }
    
    public enum QueryType {
        QUERY,
        FILTER;
    }

    public enum BooleanType {
        MUST,
        SHOULD,
        MUST_NOT;
    }
}
