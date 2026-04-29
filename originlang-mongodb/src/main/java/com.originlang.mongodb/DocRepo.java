package com.originlang.mongodb;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface DocRepo extends Repository<Doc, Long>, CrudRepository<Doc, Long> {

}
