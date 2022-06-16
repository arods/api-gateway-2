package br.com.druidess.api.gateway;

import java.io.Serializable;

public class UserDto implements Serializable {

  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
