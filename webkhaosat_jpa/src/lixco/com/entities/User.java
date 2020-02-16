package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "user")
public class User extends AbstractEntities{
	private String username;
	private String password;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
