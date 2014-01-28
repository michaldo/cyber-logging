package com.cybercom.logging.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ToStringSerializer extends JsonSerializer<Object> {

   private Class<Object> handledType;

   public ToStringSerializer() {

   }

   public ToStringSerializer(Class<Object> handledType) {
      this.handledType = handledType;
   }

   @Override
   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
         JsonProcessingException {
      if (value == null) {
         jgen.writeString("null");
      } else {
         jgen.writeString(value.toString());
      }

   }

   @Override
   public Class<Object> handledType() {
      return handledType;
   }

}
