package com.shedhack.requestbody.cache.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.stream.Collectors;

public class DefaultRequestBodyWrapper extends RequestBodyWrapper {

    private String requestBody;

    private ServletInputStream sis;


    public DefaultRequestBodyWrapper(HttpServletRequest request) throws IOException {

        super(request);
        requestBody = readBodyFromRequest(request);
    }

    private String readBodyFromRequest(HttpServletRequest request) throws IOException {

        BufferedReader bufferedReader = null;

        try {

            InputStream inputStream = request.getInputStream();
            sis = (ServletInputStream) inputStream;

            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, request.getCharacterEncoding()));
                requestBody = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            } else {
                requestBody = null;
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return requestBody;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());

        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return sis.isFinished();
            }

            @Override
            public boolean isReady() {
                return sis.isReady();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                sis.setReadListener(readListener);
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getRequestBody() {
        return requestBody;
    }
}
