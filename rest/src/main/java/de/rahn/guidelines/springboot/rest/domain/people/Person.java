package de.rahn.guidelines.springboot.rest.domain.people;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

public class Person {
	
	private String id;
	
	private String firstName;

	@NotNull
	@NotBlank
	private String lastName;
	
	@Email
	private String emailAddress;
	
	@DateTimeFormat(iso = DATE)
	@NotNull
	private LocalDate birthday;
	
	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj, false);
	}
	
	@Override
	public int hashCode() {
		return reflectionHashCode(this, false);
	}
	
	@Override
	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public LocalDate getBirthday() {
		return birthday;
	}
	
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
}