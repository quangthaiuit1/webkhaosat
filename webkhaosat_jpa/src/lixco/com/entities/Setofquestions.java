package lixco.com.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "setofquestions")
public class Setofquestions extends AbstractEntities{
	private String name;
	private String listusers;
	private String description;
	private Date starttime;
	private Date endtime;
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getListusers() {
		return listusers;
	}

	public void setListusers(String listusers) {
		this.listusers = listusers;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
}
