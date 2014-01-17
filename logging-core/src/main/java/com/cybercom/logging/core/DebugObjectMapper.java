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
 * The class is JSON Object Mapper dedicated to write objects to debug logs. The
 * main feature is special treatment byte arrays, because byte array is usually
 * content of binary file and writing to file is useless
 * 
 * @author Michal.Domagala
 * 
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
		public void serialize(byte[] value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			if (value == null) {
				jgen.writeString("null");
			} else {
				jgen.writeString("byte array length=" + value.length);
			}
		}

	}

}
