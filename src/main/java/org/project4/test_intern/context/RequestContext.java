package org.project4.test_intern.context;

import java.time.Instant;

public class RequestContext {

    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();

    private String requestId;
    private Long userId;
    private Instant timestamp;

    public RequestContext() {
    }

    public static RequestContext get() {
        return context.get();
    }

    public static void set(RequestContext requestContext) {
        context.set(requestContext);
    }

    public static void clear() {
        context.remove();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
