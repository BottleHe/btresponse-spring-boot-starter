package work.bottle.demo.model;

import java.io.Serializable;
import java.util.Map;

public abstract class Query<T> implements Serializable {
    private String sortBy;
    private String sortOrder;
    private Integer page;
    private Integer pageCnt;

    private T data;

    private Map<String, String> allowSortBy;
    private String[] queryFields;

    public Query() {
        this.page = 1;
        this.pageCnt = 20;
        this.allowSortBy = initAllowSortBy();
        this.queryFields = initQueryFields();
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        if (null == allowSortBy) {
            return;
        }
        if (!allowSortBy.containsKey(sortBy)) {
            return;
        }
        this.sortBy = allowSortBy.get(sortBy);
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        if (null == sortOrder || (sortOrder != SortOrder.ASC && sortOrder != SortOrder.DESC)) {
            this.sortOrder = "DESC";
        } else {
            this.sortOrder = sortOrder.toString();
        }
    }

    public Integer getPage() {
        if (null != this.page && this.page > 0) {
            return this.page;
        }
        return 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void nextPage() {
        this.page++;
    }

    public void prevPage() {
        this.page--;
    }

    public Integer getPageCnt() {
        if (null == this.pageCnt || this.pageCnt <= 0) {
            return 20;
        }
        return this.pageCnt;
    }

    public void setPageCnt(Integer pageCnt) {
        this.pageCnt = pageCnt;
    }

    public Integer getOffset() {
        if (null != this.page && this.page > 0) {
            if (null == this.pageCnt || this.pageCnt <= 0) {
                return (this.page - 1) * 20;
            }
            return (this.page - 1) * this.pageCnt;
        } else {
            return 0;
        }
    }

    public Integer getLength() {
        if (null == this.pageCnt || this.pageCnt <= 0) {
            return 20;
        }
        return this.pageCnt;
    }

    protected abstract Map<String, String> initAllowSortBy();
    protected abstract String[] initQueryFields();

    public void setAllowSortBy(Map<String, String> allowSortBy) {
        this.allowSortBy = allowSortBy;
    }

    public Map<String, String> getAllowSortBy() {
        return allowSortBy;
    }

    public void setQueryFields(String[] queryFields) {
        this.queryFields = queryFields;
    }

    public String[] getQueryFields() {
        return queryFields;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        private String value;

        private SortOrder(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
