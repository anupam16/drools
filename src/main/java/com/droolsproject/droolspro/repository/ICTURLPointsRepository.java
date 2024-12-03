package com.droolsproject.droolspro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.droolsproject.droolspro.model.URLPoints;

@Repository
public interface ICTURLPointsRepository  extends JpaRepository<URLPoints, Integer>{
	
//	 @Query(value = "SELECT * FROM sys.urlpoints  where urlid=:urlid and status = 'ACTIVE'", nativeQuery = true)
//	 List<URLPoints> getUrlPointsData(@Param("urlid")Integer urlid);
	 
	 @Query(value = "SELECT * FROM sys.urlpoints  where status = 'ACTIVE'", nativeQuery = true)
	 List<URLPoints> getUrlPointsData(@Param("urlid")Integer urlid);
	 
	 @Query(value = "SELECT * FROM sys.urlpoints", nativeQuery = true)
	 List<URLPoints> getUrlPointsData();
	 
	 List<URLPoints> findByNameIn(List<String> name);

	 List<URLPoints> findByTagName(String tagName);

	List<URLPoints> findByDeviceNameIn(List<String> tagName);
}
