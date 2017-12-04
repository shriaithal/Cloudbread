package edu.sjsu.cloudbread.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
@Entity
@Table(name = "scheduleData")
public class ScheduleData {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String modelId;
	private String newModelCreated;
	private Timestamp modelCreationDate;
	private Timestamp schedulerLastRunDate;

	public ScheduleData() {

	}

	public ScheduleData(Integer id, String modelId, String newModelCreated, Timestamp modelCreationDate,
			Timestamp schedulerLastRunDate) {
		super();
		this.id = id;
		this.modelId = modelId;
		this.newModelCreated = newModelCreated;
		this.modelCreationDate = modelCreationDate;
		this.schedulerLastRunDate = schedulerLastRunDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getNewModelCreated() {
		return newModelCreated;
	}

	public void setNewModelCreated(String newModelCreated) {
		this.newModelCreated = newModelCreated;
	}

	public Timestamp getModelCreationDate() {
		return modelCreationDate;
	}

	public void setModelCreationDate(Timestamp modelCreationDate) {
		this.modelCreationDate = modelCreationDate;
	}

	public Timestamp getSchedulerLastRunDate() {
		return schedulerLastRunDate;
	}

	public void setSchedulerLastRunDate(Timestamp schedulerLastRunDate) {
		this.schedulerLastRunDate = schedulerLastRunDate;
	}

	@Override
	public String toString() {
		return "ScheduleData [id=" + id + ", modelId=" + modelId + ", newModelCreated=" + newModelCreated
				+ ", modelCreationDate=" + modelCreationDate + ", schedulerLastRunDate=" + schedulerLastRunDate + "]";
	}

}
