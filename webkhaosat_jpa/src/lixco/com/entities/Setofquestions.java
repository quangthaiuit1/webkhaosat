package lixco.com.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "setofquestions")
public class Setofquestions extends AbstractEntities{
	private String name;
	private String listusers;
	private String description;
	private Timestamp starttime;
	private Timestamp endtime;
	
	
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

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}


}
