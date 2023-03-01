package com.whatachad.app.common;

public interface Error {
	String getCode();

	String getMessage(String... valus);
}
