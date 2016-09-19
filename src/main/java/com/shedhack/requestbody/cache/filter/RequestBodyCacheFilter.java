package com.shedhack.requestbody.cache.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RequestBodyCacheFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public static final List<String> WRAP_VERBS = Arrays.asList("PUT", "POST", "PATCH", "DELETE");

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

    public void destroy() {

    }

    /**
     * This implementation returns an instance of {@link DefaultRequestBodyWrapper}
     * You may wish to override this with your own specific implementation.
     *
     * @param request to wrap
     * @return RequestBodyWrapper
     * @throws IOException
     */
    protected RequestBodyWrapper getInstance(HttpServletRequest request) throws IOException {
        return new DefaultRequestBodyWrapper(request);
    }

    /**
     * Defaults to wrapping PUT/POST/PATCH/DELETE requests
     * @param request original request
     * @return flag, true means that the request should be wrapped
     */
    protected boolean wrapRequest(HttpServletRequest request){

        return WRAP_VERBS.contains(request.getMethod());
    }
}
