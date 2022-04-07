package com.example.next_level_technologies.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Component
public class DataConverterImpl implements DataConverter {


    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(String input, Class<T> type) {

        try {
            JAXBContext ctx = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            StringReader sr = new StringReader(input);

            return (T) unmarshaller.unmarshal(sr);

        } catch (JAXBException e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    public String serialize(Object o) {
        return o.toString();
    }
}
