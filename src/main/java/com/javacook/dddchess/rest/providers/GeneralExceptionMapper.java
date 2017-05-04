package com.javacook.dddchess.rest.providers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Created by javacook on 29.04.17.
 */
@Provider
public class GeneralExceptionMapper extends Exception implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        String hint = e.toString();
        Throwable cause = e.getCause();

        while (cause != null) {
            hint += " -> " + cause;
            cause = cause.getCause();
        }
        throw new WebApplicationException(hint);
    }
}
