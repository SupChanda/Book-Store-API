package com.book.store.dao.impl;

import com.book.store.dao.GenericDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
    }

    @Override
    @Transactional
    public void updateOrDeleteObject(String queryString, Map<String,Object> queryParams ){

        query = getSession().createQuery(queryString);
        String finalQueryString = queryString;
        for (Map.Entry<String, Object> queryLoop : queryParams.entrySet()) {
            query.setParameter(queryLoop.getKey(), queryLoop.getValue());
           finalQueryString = finalQueryString.replace(":" + queryLoop.getKey(), queryLoop.getValue().toString());
        }
        System.out.println("query " + finalQueryString);
        query.executeUpdate();
    }

    public String generateQueryString(String queryTemplate, Map<String, Object> templateValues){
        StringSubstitutor substitutor = new StringSubstitutor(templateValues);
        System.out.println(substitutor.replace(queryTemplate));
        return substitutor.replace(queryTemplate);
    }

    public Object getHQLQueryResultSet(String queryString){
        query = getSession().createQuery(queryString);
        return query.getResultList();
    }
    public Object getHQLSingleQueryResultSet(String queryString,Map<String,Object> queryParams){
        query = getSession().createQuery(queryString);
        String finalQueryString = queryString;
        for(Map.Entry<String,Object> queryLoop: queryParams.entrySet()){
            query.setParameter(queryLoop.getKey(),queryLoop.getValue());
            finalQueryString = finalQueryString.replace(":" + queryLoop.getKey(), queryLoop.getValue().toString());
        }
        System.out.println("finalQueryString " + finalQueryString);
        try {
            return query.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    public Object getHQLFullQueryResultSet(String queryString,Map<String,Object> queryParams){
        query = getSession().createQuery(queryString);
        String finalQueryString = queryString;
        for(Map.Entry<String,Object> queryLoop: queryParams.entrySet()){
            query.setParameter(queryLoop.getKey(),queryLoop.getValue());
            finalQueryString = finalQueryString.replace(":" + queryLoop.getKey(), queryLoop.getValue().toString());
        }
        System.out.println("finalQueryString " + finalQueryString);
        try {
            return query.getResultList();
        }catch(NoResultException ex){
            return null;
        }
    }



    @Override
    public Object getHQLQueryCount(String queryString, Map<String,Object> queryParams) {
        String finalQueryString = queryString;
        query = getSession().createQuery("SELECT COUNT(*) " + queryString);
        for(Map.Entry<String,Object> queryLoop: queryParams.entrySet()){
            query.setParameter(queryLoop.getKey(),queryLoop.getValue());
            finalQueryString = finalQueryString.replace(":" + queryLoop.getKey(), queryLoop.getValue().toString());
        }
        return query.getSingleResult();
    }

}
