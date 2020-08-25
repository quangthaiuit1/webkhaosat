package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "type_rating")
public class TypeRating extends AbstractEntities{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
