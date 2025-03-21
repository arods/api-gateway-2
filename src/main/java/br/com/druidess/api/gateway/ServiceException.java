/**
 * 
 */
package br.com.druidess.api.gateway;

/**
 * @author eduardo
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int statusCode;

	public ServiceException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
