package life.utahairquality.service;

import life.utahairquality.enums.AggregationField;
import life.utahairquality.enums.SearchField;
import life.utahairquality.model.BuilderModel;
import life.utahairquality.model.SearchQuery;
import life.utahairquality.util.QueryUtil;
import java.util.ArrayList;
import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author cbrown
 */
public class QueryBuilderService {
    
    public static QueryBuilder buildQuery(SearchQuery searchQuery) {
        ArrayList<QueryBuilder> allBuilders = new ArrayList<>();

        String terms = searchQuery.getTerms();
        
        ArrayList<BuilderModel> termModels = new ArrayList<>();
        ArrayList<BuilderModel> termModels2 = new ArrayList<>();
        ArrayList<BuilderModel> screenNameModels = new ArrayList<>();
        System.out.println("terms=" + terms);
        if (terms != null) {
            QueryUtil.addAllTerms(termModels, terms, SearchField.TEXT.getName());
            QueryUtil.addAllUsernames(screenNameModels, terms, SearchField.USER_SCREEN_NAME.getName());
        }

        // Date
        BuilderModel dateBuilder = getDateModel(searchQuery);
        if (dateBuilder != null) {
            termModels.add(dateBuilder);
        }
        
        // Place
        String place = searchQuery.getPlace();
        if (place != null) {
            termModels2.addAll(termModels);
            QueryUtil.addAllTerms(termModels, place, SearchField.TEXT.getName());
            QueryUtil.addAllTerms(termModels2, place, SearchField.PLACE_NAME.getName());
        }
        
        // Combine terms queries with each screenName
        if (screenNameModels.isEmpty()) {
            if (! termModels.isEmpty()) {
                allBuilders.add(getQueryBuilder(termModels));
            }
            if (! termModels2.isEmpty()) {
                allBuilders.add(getQueryBuilder(termModels2));
            }
        } else {
            if (! screenNameModels.isEmpty()) {
                if (screenNameModels.size() == 1) {
                    return screenNameModels.get(0).getQueryBuilder();
                } else {
                    return QueryUtil.getBooleanQuery(termModels);
                }
            } else {
                for (BuilderModel model: screenNameModels) {
                    ArrayList<BuilderModel> theseModels = new ArrayList<>();
                    theseModels.addAll(termModels);
                    theseModels.add(model);
                    allBuilders.add(
                            QueryUtil.getBooleanQuery(theseModels)
                    );

                    // Add in for the second term model is available
                    if (! termModels2.isEmpty()) {
                        theseModels.clear();
                        theseModels.addAll(termModels2);
                        theseModels.add(model);
                        allBuilders.add(
                                QueryUtil.getBooleanQuery(theseModels)
                        );
                    }
                }
            }
        }
        
        // Return the minimum query structure.
        if (allBuilders.isEmpty()) {
            // If a date range is specified, then return a match all query.
            if (searchQuery.getStartDate() != null || searchQuery.getEndDate() != null) {
                return getDateQuery(searchQuery);
            } else {
                return QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), DateTime.now().minusHours(24).toString(ISODateTimeFormat.dateTime()), null);
            }
        } else {
            // If there is only one builder, then we don't need a dismax query.
            if (allBuilders.size() == 1) {
                return allBuilders.get(0);
            } else {
                return QueryUtil.getDisMaxQueryBuilder(allBuilders);
            }
        }
    }
    
    public static SearchQuery setPaging(SearchQuery searchQuery) {
        // Set page sizes to passed in values if not already set in query parser
        if (searchQuery.getPageSize() < 1) {
            searchQuery.setPageSize(ClientService.SIZE);
        }
        if (searchQuery.getPageFrom() < 1) {
            searchQuery.setPageFrom((searchQuery.getPage() -1)  * searchQuery.getPageSize());
        }
        
        return searchQuery;
    }
    
    private static BuilderModel getDateModel(SearchQuery searchQuery) {
        QueryBuilder dateBuilder = getDateQuery(searchQuery);
        if (dateBuilder != null) {
            return new BuilderModel(dateBuilder,BuilderModel.QueryType.QUERY, BuilderModel.BooleanType.MUST);
        }
        
        return null;
    }
    
    private static QueryBuilder getDateQuery(SearchQuery searchQuery) {
        String startDateString = null;
        DateTime startDate = null;
        String endDateString = null;
        DateTime endDate = null;

        if (searchQuery.getStartDate() != null) {
            startDateString = searchQuery.getStartDate().replaceAll("\"", "");
            startDate = DateTime.parse(startDateString);
        }
        if (searchQuery.getEndDate() != null) {
            endDateString = searchQuery.getEndDate().replaceAll("\"", "");
            endDate = DateTime.parse(endDateString);
        }
        
        if (startDate != null && endDate != null) {
            return QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), startDateString, endDateString);
        } else if (startDate != null) {
            return QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), startDateString, null);
        } else if (endDate != null) {
            return QueryUtil.getRangeQueryBuilder(AggregationField.CREATED_AT.getName(), null, endDateString);
        }

        return null;
    }

    private static QueryBuilder getQueryBuilder(ArrayList<BuilderModel> builders) {
        if ((builders.size() > 1) || (builders.get(0).getBooleanType() != BuilderModel.BooleanType.MUST)) {
//            if (builders.get(0).getQueryType().equals(QueryType.QUERY)) {
            return QueryUtil.getBooleanQuery(builders);
//            } else {
//                queryBuilder = QueryUtil.getFilteredQuery(null, builders.get(0).getFilterBuilder());
//            }
        } else {
            return builders.get(0).getQueryBuilder();
        }
    }
}
