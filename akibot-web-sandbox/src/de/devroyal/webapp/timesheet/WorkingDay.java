package de.devroyal.webapp.timesheet;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "T_WORKINGDAY")
public class WorkingDay {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "DAY")
	private Date date;

	@Column(name = "BEGINTIME")
	private Time beginTime;

	@Column(name = "ENDTIME")
	private Time endTime;

	@Column(name = "HOURS")
	private float hours;

	@Column(name = "DESCRIPTION")
	private String description;

	public WorkingDay() {

	}

	public WorkingDay(int id, Date date, Time beginTime, Time endTime, float hours, String description) {
		super();
		this.id = id;
		this.date = date;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.hours = hours;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Time beginTime) {
		this.beginTime = beginTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
