package edu.sjsu.cloudbread.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import edu.sjsu.cloudbread.dao.ScheduleDataDao;
import edu.sjsu.cloudbread.model.ScheduleData;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Repository("scheduleDataDao")
@Transactional
@SuppressWarnings("unchecked")
public class ScheduleDataDaoImpl implements ScheduleDataDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ScheduleData getLatestData() {

		List<ScheduleData> list = sessionFactory.getCurrentSession()
				.createQuery(" from ScheduleData order by schedulerLastRunDate desc ").setMaxResults(1).list();
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void insert(ScheduleData newScheduleData) {
		sessionFactory.getCurrentSession().save(newScheduleData);
	}

}
