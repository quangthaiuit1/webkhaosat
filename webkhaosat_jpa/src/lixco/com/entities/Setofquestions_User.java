package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="setofquestions_user")
public class Setofquestions_User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	private Setofquestions setofquestions;
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Setofquestions getSetofquestions() {
		return setofquestions;
	}
	public void setSetofquestions(Setofquestions setofquestions) {
		this.setofquestions = setofquestions;
	}
}
