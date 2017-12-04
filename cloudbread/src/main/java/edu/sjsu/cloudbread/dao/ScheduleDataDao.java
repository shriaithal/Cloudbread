package edu.sjsu.cloudbread.dao;

import edu.sjsu.cloudbread.model.ScheduleData;

public interface ScheduleDataDao {

	ScheduleData getLatestData();

	void insert(ScheduleData newScheduleData);

}
