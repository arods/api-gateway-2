/**
 * 
 */
package br.com.druidess.api.gateway;

import java.io.Serializable;

/**
 * @author eduardo
 *
 */
public class ValidateTokenDtoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private UserDto userDto;

	private String message;

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ValidateTokenDtoResponse [userDto=" + userDto + ", message=" + message + "]";
	}

}
