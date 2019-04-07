package de.rahn.guidelines.springboot.jpa.domain.people;

import static javax.persistence.AccessType.FIELD;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Access(FIELD)
@Entity
public class Person extends AbstractAuditing {

  @Id
  @GeneratedValue(generator = "uuid")
  private String id;

  private String firstName;

  @Column(nullable = false)
  @NotNull
  @NotBlank
  private String lastName;

  @Email
  private String emailAddress;

  @DateTimeFormat(iso = DATE)
  @NotNull
  private LocalDate birthday;

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
