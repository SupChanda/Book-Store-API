package com.book.store.dao.impl;

import com.book.store.dao.GenericDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.apache.commons.text.StringSubstitutor;
import org.hibernate.Transaction;

import java.util.Map;

public class GenericDaoImpl<T> implements GenericDao<T> {
    @PersistenceContext
    EntityManager entityManager;
    Query query;
    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    @Override
    public void saveOrUpdate(T entity) {
        if(entity == null){
            return;
        }
        Session session = getSession();
        session.saveOrUpdate(entity);
        //session.merge(entity);
        // session.flush();
//        session.clear();
    }
    @Override
    public void updateOrDeleteUser(String queryString, Map<String,Object> queryParams ){

        Transaction transaction = null;
        transaction = getSession().beginTransaction();
        query = getSession().createQuery(queryString);
        String finalQueryString = queryString;
        for (Map.Entry<String, Object> queryLoop : queryParams.entrySet()) {
            query.setParameter(queryLoop.getKey(), queryLoop.getValue());
           // finalQueryString = finalQueryString.replace(":" + queryLoop.getKey(), queryLoop.getValue().toString());
        }
        //System.out.println("query " + finalQueryString);
        query.executeUpdate();
        transaction.commit();


        //T entityDB = session.get(entityClass,entityId);
    }

    public String generateQueryString(String queryTemplate, Map<String, Object> templateValues){
        StringSubstitutor substitutor = new StringSubstitutor(templateValues);
        return substitutor.replace(queryTemplate);
    }
    public Object getHQLSingleQueryResultSet(String queryString,Map<String,Object> queryParams){
        query = getSession().createQuery(queryString);
        for(Map.Entry<String,Object> queryLoop: queryParams.entrySet()){
            query.setParameter(queryLoop.getKey(),queryLoop.getValue());
        }
        return query.getSingleResult();
    }
    @Override
    public Long getHQLQueryCount(String queryString, Map<String,Object> queryParams) {
        query = getSession().createQuery("SELECT COUNT(*) " + queryString);
        for(Map.Entry<String,Object> queryLoop: queryParams.entrySet()){
            query.setParameter(queryLoop.getKey(),queryLoop.getValue());
        }
//        System.out.println("query: " + query);
//        System.out.println(query.getSingleResult());
        return (Long) query.getSingleResult();
    }




}
