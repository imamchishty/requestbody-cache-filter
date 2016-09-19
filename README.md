# request-body-cache-filter

[![Build Status](https://travis-ci.org/imamchishty/requestbody-cache-filter.svg?branch=master "requestbody-cache-filter")](https://travis-ci.org/imamchishty/requestbody-cache-filter) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.requestbody/cache-filter/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.requestbody/cache-filter)

## Introduction
Reading the inputstream from a servlet is not supported out of the box. The reader method on a request object is usually consumed by MVC frameworks such as Spring. This in most cases is appropriate as you may not care about reading it multiple times. There are some scenarios when you wish to be able to read the request body multiple times, e.g. logging, tracing, exception handling etc.
For such scenarios this component is ideal. The component will provide a cache of the request body and will make it available via a thread local object. In order to achieve this a servlet filter (RequestBodyCacheFilter) was created.

## RequestBodyCacheFilter
This is the core filter which is responsible for wrapping existing requests and caching the request body. The default behaviour is to wrap requests for POST, PUT, DELETE and PATCH verbs. You can easily customize this as explained later.

The code is extremely simple:

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {

            if(wrapRequest((HttpServletRequest) request)) {

                // create cache
                RequestBodyWrapper wrapper = getInstance((HttpServletRequest) request);

                // set thread local
                RequestBodyThreadLocal.set(wrapper.getRequestBody());

                // continue
                chain.doFilter(wrapper, response);
            }
            else {
                // as you were seageant
                chain.doFilter(request, response);
            }
        }
        finally {

            // clean up
            RequestBodyThreadLocal.clear();
        }
    }

## RequestBodyWrapper
The code snippet from above shows that we have a method called `getInstance` returning a type RequestBodyWrapper. This is an abstract class as follows:

  public abstract class RequestBodyWrapper extends HttpServletRequestWrapper {
  
      public RequestBodyWrapper(HttpServletRequest request) {
          super(request);
      }
  
      public abstract String getRequestBody() throws IOException;
    }

A default implementation is provided, and if this isn't good enough for your needs then you can override the `getInstance()` method and provide your own custom implementation of `RequestBodyWrapper`.

## Extensions
Custom `RequestBodyWrapper` can be added via extending that abstract class and by overriding the `getInstance()` method on `RequestBodyCacheFilter`.

If you wish to support different HTTP verbs simply override the `wrapRequest` method which simply return true or false;

## Thread Local
If the request body has been found, it will be set on `RequestBodyThreadLocal`. To access it:

`RequestBodyThreadLocal.get()`

This will be cleared by the filter automatically.

## Configuration with Spring

    @Bean
    public Filter requestBodyFilter() {
        return new RequestBodyCacheFilter();
    }

You could also use a filter registration bean.

## Sister projects

[Request Body Cache Interceptor](https://github.com/imamchishty/requestbody-cache-interceptor).
