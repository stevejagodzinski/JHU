package com.jagodzinski.simplebanking.controller.dataformat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLResponseFormatStrategy implements ResponseStrategy {
	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			stream.write("<response>".getBytes());

			for (Object responseObject : responseObjects) {
				Marshaller marshaller = JAXBContext.newInstance(responseObject.getClass()).createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
				marshaller.marshal(responseObject, stream);
			}

			stream.write("</response>".getBytes());
			return stream.toString();
		} catch (IOException | JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
