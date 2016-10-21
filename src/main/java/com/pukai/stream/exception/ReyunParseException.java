package com.pukai.stream.exception;

public class ReyunParseException extends ReyunException {
	private static final long serialVersionUID = 8213513771508032638L;

	public ReyunParseException() {
        super();
    }
	
	public ReyunParseException(String message) {
        super(message);
    }
	
	public ReyunParseException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public ReyunParseException(Throwable cause) {
        super(cause);
    }
}
