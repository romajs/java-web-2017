package com.romajs.jaxrs.interceptor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@Provider
public class GZipEncoder implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		context.setOutputStream(new GZIPOutputStream(context.getOutputStream()));
		context.getHeaders().putSingle("Content-Encoding", "gzip");
		context.proceed();
	}
}