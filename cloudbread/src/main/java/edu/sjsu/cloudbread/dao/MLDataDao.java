package edu.sjsu.cloudbread.dao;

import java.util.List;

import edu.sjsu.cloudbread.model.MLData;

public interface MLDataDao {

	void insertMLData(List<MLData> results);
	
	List<List> fetchMLData(String userName);

}
