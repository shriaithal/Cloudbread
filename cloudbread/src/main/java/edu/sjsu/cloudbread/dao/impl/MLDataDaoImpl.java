package edu.sjsu.cloudbread.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.CollectionUtils;

import edu.sjsu.cloudbread.dao.MLDataDao;
import edu.sjsu.cloudbread.dao.ScheduleDataDao;
import edu.sjsu.cloudbread.model.MLData;
import edu.sjsu.cloudbread.model.ScheduleData;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Repository("mlDataDao")
@Transactional
public class MLDataDaoImpl implements MLDataDao {

	@Autowired
	ScheduleDataDao scheduleDataDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * @author Anushri Srinath Aithal
	 */
	@Override
	public void insertMLData(List<MLData> results) {
		for (MLData mlData : results) {
			sessionFactory.getCurrentSession().save(mlData);
		}
	}
	
	/**
	 * 
	 * @author Ashwini Shankar Narayan
 	 *
 	**/
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	@Override
	public List<List> fetchMLData(String userName) {
		ScheduleData scheduleData = scheduleDataDao.getLatestData();
		Timestamp latestRun = scheduleData.getSchedulerLastRunDate();
		
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(latestRun);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        
        Timestamp latestRunback = latestRun;
        Timestamp latestRunnext = latestRun;
         
        //Get last 5 days date from latest schedule run
         Calendar cal = Calendar.getInstance();
         Timestamp fiveDaysAgo = new Timestamp(cal.getTimeInMillis());
         cal.setTime(latestRunback);
         cal.add(Calendar.DATE, -5);
         cal.set(Calendar.MILLISECOND, 0);
         cal.set(Calendar.SECOND, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.HOUR_OF_DAY, 0);      
         fiveDaysAgo = new java.sql.Timestamp(cal.getTimeInMillis());
      
        //Get next 5 days date from latest schedule run
         Calendar cal1 = Calendar.getInstance();
         Timestamp fiveDaysNext = new Timestamp(calendar.getTimeInMillis());
         cal1.setTime(latestRunnext);
         cal1.add(Calendar.DATE, 6);
         cal1.set(Calendar.MILLISECOND, 0);
         cal1.set(Calendar.SECOND, 0);
         cal1.set(Calendar.MINUTE, 0);
         cal1.set(Calendar.HOUR_OF_DAY, 0);
         fiveDaysNext = new java.sql.Timestamp(cal1.getTimeInMillis());
         
         Query query = sessionFactory.getCurrentSession()
 				.createQuery("from MLData where name= :userName and actualTime between :start and :end ORDER BY actualTime");
 		
 		query.setParameter("userName", userName);
		query.setParameter("start", fiveDaysAgo);
		query.setParameter("end", fiveDaysNext);
       
		List<List> toReturn = new ArrayList<List>();
		List<MLData> mlData = query.list();
		List<String> Days = new ArrayList<String>();
		List<Integer> Date = new ArrayList<Integer>();
		Days.add("Sunday");
		Days.add("Monday");
		Days.add("Tuesday");
		Days.add("Wednesday");
		Days.add("Thursday");
		Days.add("Friday");
		Days.add("Saturday");
		List<MLData> actualData = new ArrayList<MLData>();
		List<MLData> predictedData = new ArrayList<MLData>();
		if (!CollectionUtils.isNullOrEmpty(mlData)) {
			for(MLData data:mlData){
				if(data.getPredicted().equals("Y")) {
					predictedData.add(data);
				}
				else {
					actualData.add(data);
				}
			}
		}
		
		List<BigDecimal> series1 = new ArrayList<BigDecimal>();
		List<String> series2 = new ArrayList<String>();
//		List<Integer> idx = new ArrayList<Integer>(Collections.nCopies(mlData.size(), 0));
		if (!CollectionUtils.isNullOrEmpty(mlData)) {
		for(int i=0; i<actualData.size();i++) {
			double wastage = actualData.get(i).getWastage().doubleValue(); 
			int count = 1;
			for(int j = i+1;j<actualData.size();j++) {
				if(actualData.get(i).getActualTime().getDate() == (actualData.get(j).getActualTime().getDate())) {
					wastage+=actualData.get(j).getWastage().doubleValue();
					count++;
					i=j;
				}
				
			}
			series1.add(new BigDecimal(wastage/count));
			series2.add(Days.get(actualData.get(i).getActualTime().getDay()));
		
		}

		toReturn.add(series1);
		toReturn.add(series2);
		
		 series1 = new ArrayList<BigDecimal>();
		 series2 = new ArrayList<String>();
		for(MLData data:predictedData){
			series1.add(data.getWastage());
			series2.add(Days.get(data.getActualTime().getDay()));
			//series2.add(Date.get(data.getActualTime().getDate()));
		}
		toReturn.add(series1);
		toReturn.add(series2);
		
		}
		
//		if (!CollectionUtils.isNullOrEmpty(mlData)) {
//			List<BigDecimal> series1 = new ArrayList<BigDecimal>();
//			List<String> series2 = new ArrayList<String>();
//			//List<Integer> series2 = new ArrayList<Integer>();
//			
//
//			for(MLData data:mlData){
//				series1.add(data.getWastage());
//				series2.add(Days.get(data.getActualTime().getDay()));
//				//series2.add(Date.get(data.getActualTime().getDate()));
//			}
//			toReturn.add(series1);
//			toReturn.add(series2);
//		}
		return toReturn;
		}
	
	}

