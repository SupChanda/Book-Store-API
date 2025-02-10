package com.book.store.dao;

import java.util.Map;

public interface GenericDao<T>{
    void saveOrUpdate(T entity);
    void update(String queryString, Map<String,Object> queryParams);
    String generateQueryString(String queryTemplate,Map<String,Object> templateValues);
    Long getHQLQueryCount(String queryString,Map<String,Object> queryParams);

    Object getHQLSingleQueryResultSet(String queryString,Map<String,Object> queryParams);

}
