package com.book.store.dao;

import java.util.Map;

public interface GenericDao<T>{
    void saveOrUpdate(T entity);
    String generateQueryString(String queryTemplate,Map<String,Object> templateValues);
    Long getHQLQueryCount(String queryString,Map<String,Object> queryParams);

    Object getHQLSingleQueryResultSet(String queryString,Map<String,Object> queryParams);
    Object getHQLQueryResultSet(String queryString);

    void updateOrDeleteUser(String queryString, Map<String,Object> queryParams);


}
