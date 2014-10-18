package life.utahairquality.service;

import life.utahairquality.enums.SearchField;
import life.utahairquality.model.BuilderModel;
import life.utahairquality.model.BuilderModel.BooleanType;
import org.elasticsearch.index.query.QueryBuilder;
import java.util.ArrayList;
import life.utahairquality.model.SearchQuery;
import life.utahairquality.util.QueryUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.Client;

/**
 * 
 * @author cbrown
 */
public class DeleteService implements Serializable {
    private static final Logger log = Logger.getLogger(DeleteService.class.getName());
    private final Client client = ClientService.getClient();
    
    /**
     * 
     * @param sq
     * @return 
     */
    public boolean deleteByQuery(SearchQuery sq) {

        ArrayList<BuilderModel> builders = new ArrayList<>();
        if (sq.getDocId()!= null) {
            QueryUtil.addAllUsernames(builders, sq.getDocId(), SearchField.ID.getName());
        } else if (sq.getTerms() != null) {
            QueryUtil.addAllTerms(builders, sq.getTerms(), SearchField.TEXT.getName());
//        } else if (sq.getScreenName() != null) {
//            QueryUtil.allAllTermsUncleaned(builders, sq.getScreenName(), SearchField.USER_SCREEN_NAME.getName());
        } else {
            return false;
        }

        QueryBuilder qb;
        if ((builders.size() > 1) || (builders.get(0).getBooleanType() != BooleanType.MUST)) {
            qb = QueryUtil.getBooleanQuery(builders);
        } else {
            qb = builders.get(0).getQueryBuilder();
        }
        
        DeleteByQueryRequestBuilder builder = client.prepareDeleteByQuery(ClientService.INDEX)
                .setQuery(qb);
        
        DeleteByQueryResponse response = builder.execute().actionGet();
        
        return true;
    }
}
