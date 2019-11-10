package com.emailfilter.exceptions;

public class DuplicateLabelException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final ErrorCode code;

	public DuplicateLabelException(ErrorCode code) {
		super();
		this.code = code;
	}

	public DuplicateLabelException(String message, Throwable cause, ErrorCode code) {
		super(message, cause);
		this.code = code;
	}

	public DuplicateLabelException(String message, ErrorCode code) {
		super(message);
		this.code = code;
	}

	public DuplicateLabelException(Throwable cause, ErrorCode code) {
		super(cause);
		this.code = code;
	}

	public ErrorCode getCode() {
		return this.code;
	}
}
