package dk.lyngby.dao;

import java.util.List;

public interface IDAO<T>
{
    List<T> getAll();

    T getById(long id);

    void create(T t);

    void update(T t);

    void delete(long id);
}