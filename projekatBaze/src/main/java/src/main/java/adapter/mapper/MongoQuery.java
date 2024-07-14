package src.main.java.adapter.mapper;

import java.util.*;

public class MongoQuery {
    private List<String> selectFields;

    private String collectionName;

    private Map<Object, Object> whereConditions;

    private List<String> groupByFields;


    public MongoQuery() {
        selectFields = new ArrayList<>();
        whereConditions = new HashMap<>();
        groupByFields = new ArrayList<>();
    }

    public List<String> getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(List<String> selectFields) {
        this.selectFields = selectFields;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Map<Object, Object> getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(Map<Object, Object> whereConditions) {
        this.whereConditions = whereConditions;
    }

    public List<String> getGroupByFields() {
        return groupByFields;
    }

    public void setGroupByFields(List<String> groupByFields) {
        this.groupByFields = groupByFields;
    }


    @Override
    public int hashCode() {
        return Objects.hash(whereConditions);
    }
}