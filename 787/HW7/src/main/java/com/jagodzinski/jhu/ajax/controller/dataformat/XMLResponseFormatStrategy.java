package com.jagodzinski.jhu.ajax.controller.dataformat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLResponseFormatStrategy implements ResponseStrategy {

	private static XMLResponseFormatStrategy INSTANCE = new XMLResponseFormatStrategy();

	public static XMLResponseFormatStrategy getInstance() {
		return INSTANCE;
	}

	private XMLResponseFormatStrategy() {
	}

	@Override
	public String toResponse(Collection<? extends Object> responseObjects) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			stream.write("<response>".getBytes());

			for (Object responseObject : responseObjects) {
				marshallObject(responseObject, stream);
			}

			stream.write("</response>".getBytes());
			return stream.toString();
		} catch (IOException | JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	private void marshallObject(Object responseObject, OutputStream stream) throws JAXBException, IOException {
		if (responseObject == null) {
			stream.write("<null />".getBytes());
		} else {
			Marshaller marshaller = JAXBContext.newInstance(responseObject.getClass()).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			marshaller.marshal(responseObject, stream);
		}
	}
}
