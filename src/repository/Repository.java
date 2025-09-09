package repository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface Repository<T,ID> {
    public void save(T entity) throws SQLException;
    public T findById(ID id) throws SQLException;
    public void deleteById(ID id) throws SQLException;
    public void update(T entity) throws SQLException;
    public Collection<T> findAll() throws SQLException;
    public boolean existsById(ID id) throws SQLException;
    public int count() throws SQLException;
}
