package com.pukai.stream.exception;

public class ReyunException extends RuntimeException {
	private static final long serialVersionUID = 5281837596525902071L;
	
	public ReyunException() {
        super();
    }
	
	public ReyunException(String message) {
        super(message);
    }
	
	public ReyunException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public ReyunException(Throwable cause) {
        super(cause);
    }
}
