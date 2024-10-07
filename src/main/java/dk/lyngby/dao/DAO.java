package dk.lyngby.dao;

import java.util.List;

public abstract class DAO<T> implements IDAO<T>
{

    @Override
    public abstract List<T> getAll();

    @Override
    public abstract T getById(long id);

    @Override
    public abstract void create(T t);

    @Override
    public abstract void update(T t);

    @Override
    public abstract void delete(long id);
}