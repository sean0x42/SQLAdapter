package io.seanbailey.model;

import io.seanbailey.adapter.Model;
import io.seanbailey.adapter.annotation.Excluded;
import io.seanbailey.adapter.annotation.PrimaryKey;
import java.util.UUID;

public class User extends Model {

  @PrimaryKey
  private UUID id;

  private String username;

  @Excluded
  private Boolean ignored = true;


  /**
   * Constructs a new user.
   *
   * @since 2018-05-14
   */
  public User() {
    id = null;
    username = null;
  }

  /**
   * Constructs a new user.
   *
   * @param username Name of the user.
   * @since 2018-05-14
   */
  public User(String username) {
    this.id = UUID.randomUUID();
    this.username = username;
  }


  /**
   * Attempts to validate the model before any database operations are carried out.
   *
   * @return Whether this model is considered valid.
   * @see #beforeValidate()
   * @see #afterValidate()
   * @since 2018-05-14
   */
  @Override
  public boolean validate() {
    return true;
  }


  public String getId() {
    return id.toString();
  }

  public void setId(String id) {
    this.id = UUID.fromString(id);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
