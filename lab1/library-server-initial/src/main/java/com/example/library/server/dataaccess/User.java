package com.example.library.server.dataaccess;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class User extends AbstractPersistable<Long> {

  @NotNull private UUID identifier;

  @NotNull
  @Size(min = 1, max = 100)
  @Email
  private String email;

  @NotNull
  @Size(min = 8, max = 200)
  private String password;

  @NotNull
  @Size(min = 1, max = 100)
  private String firstName;

  @NotNull
  @Size(min = 1, max = 100)
  private String lastName;

  @ElementCollection private List<String> roles = new ArrayList<>();

  public User() {}

  public User(User user) {
    this(
        user.getIdentifier(),
        user.getEmail(),
        user.getPassword(),
        user.getFirstName(),
        user.getLastName(),
        user.getRoles());
  }

  @PersistenceConstructor
  public User(
      UUID identifier,
      String email,
      String password,
      String firstName,
      String lastName,
      List<String> roles) {
    this.identifier = identifier;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    this.identifier = identifier;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    User user = (User) o;
    return identifier.equals(user.identifier)
        && email.equals(user.email)
        && password.equals(user.password)
        && firstName.equals(user.firstName)
        && lastName.equals(user.lastName)
        && Objects.equals(roles, user.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), identifier, email, password, firstName, lastName, roles);
  }

  @Override
  public String toString() {
    return "User{"
        + "identifier="
        + identifier
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", roles="
        + roles
        + '}';
  }
}
