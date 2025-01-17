package br.com.mercadolivre.desafioquality.repository;

import br.com.mercadolivre.desafioquality.exceptions.DatabaseManagementException;
import br.com.mercadolivre.desafioquality.exceptions.DatabaseReadException;
import br.com.mercadolivre.desafioquality.exceptions.DatabaseWriteException;
import br.com.mercadolivre.desafioquality.exceptions.DbEntryAlreadyExists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApplicationRepository<T, K> {
    public List<T> read() throws DatabaseReadException, DatabaseManagementException;
    public Optional<T> find(K id) throws DatabaseReadException, DatabaseManagementException;
    public T add(T listToAdd) throws DatabaseWriteException, DatabaseReadException, DbEntryAlreadyExists, DatabaseManagementException;
    public Integer update(Map<String, Object> filters, Map<String, Object> values) throws DatabaseReadException, DatabaseWriteException, DatabaseManagementException;
}
