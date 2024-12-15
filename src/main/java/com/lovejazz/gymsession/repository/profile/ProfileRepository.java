package com.lovejazz.gymsession.repository.profile;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository<T> {

    public List<T> findAll();
    public Optional<T> findById(Integer id);

    public  void  create(T profile);

    public  void  update(T profile,Integer id);

    public  void  delete(Integer id);

}
