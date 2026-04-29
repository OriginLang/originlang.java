package com.originlang.data.base;

import jakarta.data.repository.By;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepository<T, ID> {

	@Save
	<S extends T> S save(S entity);

	@Find
	Optional<T> findById(@By(By.ID) ID id);

	@Find
	List<T> findAll();

	@Delete
	void deleteById(@By(By.ID) ID id);

}
