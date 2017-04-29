package com.javacook.dddchess.rest.providers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Created by vollmer on 29.04.17.
 */
@Provider
public class RestException extends Exception implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        // e.printStackTrace();
        throw new WebApplicationException(e);
    }
}
