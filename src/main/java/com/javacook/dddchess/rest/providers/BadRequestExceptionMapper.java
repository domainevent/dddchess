package com.javacook.dddchess.rest.providers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    public Response toResponse(BadRequestException exception) {

        return Response.status(Response.Status.BAD_REQUEST).
                entity(Response.status(Response.Status.NOT_FOUND)
                        .entity(exception.getMessage()).type(MediaType.TEXT_PLAIN).build()).
                build();
    }
}