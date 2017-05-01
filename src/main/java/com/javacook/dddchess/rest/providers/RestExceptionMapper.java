package com.javacook.dddchess.rest.providers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Created by javacook on 29.04.17.
 */
@Provider
public class RestExceptionMapper extends Exception implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        // e.printStackTrace();
        throw new WebApplicationException(e);
    }
}
