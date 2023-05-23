package com.whatachad.app.common;

public class CommonException extends RuntimeException {

	private Class errorType;
	private String code;
	private String message;

	private CommonException(String message) {
		super(message);
		this.message = message;
	}

	public CommonException(String code, String message) {
		this(message);
		this.code = code;
	}

	private CommonException(String message, Throwable err) {
		super(message, err);
		this.message = message;
	}

	public CommonException(String code, String message, Throwable err) {
		this(message, err);
		this.code = code;
	}

	public CommonException(Error error, String... args) {
		this(error.getCode(), error.getMessage(args));
		this.errorType = error.getClass();
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public boolean isIError() {
		return this.errorType.equals(IError.class);
	}

	public boolean isBError() {
		return this.errorType.equals(BError.class);
	}

	public boolean isIError(IError iErrMsg) {
		return this.getCode().equals(iErrMsg.getCode());
	}
}

