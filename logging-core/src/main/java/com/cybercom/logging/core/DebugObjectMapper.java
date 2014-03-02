/**
 * The MIT License
 * Copyright (c) 2014 Michal Domagala
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cybercom.logging.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * The class is JSON Object Mapper dedicated to write objects to debug logs. The main feature is special treatment byte
 * arrays, because byte array is usually content of binary file and writing to file is useless
 * 
 * @author Michal.Domagala
 */
public class DebugObjectMapper extends ObjectMapper {

   private SimpleModule myModule;

   public DebugObjectMapper() {
      myModule = new SimpleModule();
      myModule.addSerializer(new ByteArraySerializer());
      registerModule(myModule);
      configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
   }

   public void addJsonSerializer(JsonSerializer<?> jsonSerializer) {
      myModule.addSerializer(jsonSerializer);
   }

   class ByteArraySerializer extends JsonSerializer<byte[]> {

      @Override
      public Class<byte[]> handledType() {
         return byte[].class;
      }

      @Override
      public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
         if (value == null) {
            jgen.writeString("null");
         } else {
            jgen.writeString("byte array length=" + value.length);
         }
      }

   }

}
