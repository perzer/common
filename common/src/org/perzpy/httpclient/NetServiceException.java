package org.perzpy.httpclient;

public class NetServiceException extends Exception {
	public NetServiceException(String msg, Throwable e) {
		super(msg, e);
	}
}
