package com.msmeli.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

public class DecimalJsonSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString( String.format(Locale.GERMAN,"%,.2f", aDouble));
    }
}
