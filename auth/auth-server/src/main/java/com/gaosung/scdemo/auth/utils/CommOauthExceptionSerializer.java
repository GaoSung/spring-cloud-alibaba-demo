package com.gaosung.scdemo.auth.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gaosung.scdemo.auth.common.CommonResponse;
import com.gaosung.scdemo.auth.handler.CommOauthException;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommOauthExceptionSerializer extends StdSerializer<CommOauthException> {

    public CommOauthExceptionSerializer() {
        super(CommOauthException.class);
    }

    @Override
    public void serialize(CommOauthException exception, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Gson gson = new Gson();
        jsonGenerator.writeRawValue(gson.toJson(CommonResponse.fail(exception.getHttpErrorCode(), exception.getMessage())));
    }
}
