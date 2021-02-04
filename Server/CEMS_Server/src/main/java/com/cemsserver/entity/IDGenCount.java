package com.cemsserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IDGenCount
{
	@Id
	Integer ID;
	
	Integer studentIDCount;
	Integer staffIDCount;
	Integer eventIDCount;
	Integer eventRegIDCount;
	
	public IDGenCount() { super(); }

	public IDGenCount(Integer iD, Integer studentIDCount, Integer staffIDCount, Integer eventIDCount,
			Integer eventRegIDCount)
	{
		super();
		ID = iD;
		this.studentIDCount = studentIDCount;
		this.staffIDCount = staffIDCount;
		this.eventIDCount = eventIDCount;
		this.eventRegIDCount = eventRegIDCount;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer iD)
	{
		ID = iD;
	}

	public Integer getStudentIDCount()
	{
		return studentIDCount;
	}

	public void setStudentIDCount(Integer studentIDCount)
	{
		this.studentIDCount = studentIDCount;
	}

	public Integer getStaffIDCount()
	{
		return staffIDCount;
	}

	public void setStaffIDCount(Integer staffIDCount)
	{
		this.staffIDCount = staffIDCount;
	}

	public Integer getEventIDCount()
	{
		return eventIDCount;
	}

	public void setEventIDCount(Integer eventIDCount)
	{
		this.eventIDCount = eventIDCount;
	}

	public Integer getEventRegIDCount()
	{
		return eventRegIDCount;
	}

	public void setEventRegIDCount(Integer eventRegIDCount)
	{
		this.eventRegIDCount = eventRegIDCount;
	}

	@Override
	public String toString()
	{
		return "IDGenCount [ID=" + ID + ", studentIDCount=" + studentIDCount + ", staffIDCount=" + staffIDCount
				+ ", eventIDCount=" + eventIDCount + ", eventRegIDCount=" + eventRegIDCount + "]";
	}
}
