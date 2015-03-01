package com.devlib.crawler;

public class DeepException extends Exception {

	private static final long serialVersionUID = 2793267457283040355L;

	public DeepException(String errString) {
		super(errString);
	}

	public DeepException(String errString, Throwable cause) {
		super(errString, cause);
	}

	public DeepException(Throwable cause) {
		super(cause);
	}

}
