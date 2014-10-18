package life.utahairquality.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public class SearchQuery implements Serializable {    
    private String terms;
    private String place;
    private String startDate;
    private String endDate;
    private Boolean sentiment;
    private String docId;
    private String beginAt;
    
    private String timeMillis;
    private long totalResults;
    private int availableResults;
            
    private int page = 1;
    private int pageFrom = -1;
    private int pageSize = -1;
    
    public SearchQuery() {}

    @JsonProperty("terms")
    public String getTerms() {
        return terms;
    }

    @JsonProperty("terms")
    public void setTerms(String terms) {
        this.terms = terms;
    }

    @JsonProperty("place")
    public String getPlace() {
        return place;
    }

    @JsonProperty("place")
    public void setPlace(String place) {
        this.place = place;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("docId")
    public String getDocId() {
        return docId;
    }

    @JsonProperty("sentiment")
    public Boolean getSentiment() {
        return sentiment;
    }

    @JsonProperty("sentiment")
    public void setSentiment(Boolean sentiment) {
        this.sentiment = sentiment;
    }

    @JsonProperty("docId")
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @JsonProperty("beginAt")
    public String getBeginAt() {
        return beginAt;
    }

    @JsonProperty("beginAt")
    public void setBeginAt(String beginAt) {
        this.beginAt = beginAt;
    }

    @JsonProperty("timeMillis")
    public String getTimeMillis() {
        return timeMillis;
    }

    @JsonProperty("timeMillis")
    public void setTimeMillis(String timeMillis) {
        this.timeMillis = timeMillis;
    }

    @JsonProperty("totalResults")
    public long getTotalResults() {
        return totalResults;
    }

    @JsonProperty("totalResults")
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("availableResults")
    public int getAvailableResults() {
        return availableResults;
    }

    @JsonProperty("availableResults")
    public void setAvailableResults(int availableResults) {
        this.availableResults = availableResults;
    }

    @JsonProperty("page")
    public int getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(int page) {
        this.page = page;
    }

    @JsonProperty("pageFrom")
    public int getPageFrom() {
        return pageFrom;
    }

    @JsonProperty("pageFrom")
    public void setPageFrom(int pageFrom) {
        this.pageFrom = pageFrom;
    }

    @JsonProperty("pageSize")
    public int getPageSize() {
        return pageSize;
    }

    @JsonProperty("pageSize")
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("terms=").append(terms).append(" ");
        builder.append("id=").append(docId).append("\n");
        return builder.toString();
    }
}
