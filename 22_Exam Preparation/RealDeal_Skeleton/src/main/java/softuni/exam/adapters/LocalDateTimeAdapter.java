package softuni.exam.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String src) throws Exception {
        return LocalDateTime
                .parse(src, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
    }

    @Override
    public String marshal(LocalDateTime date) throws Exception {
        //return date.toString();
        //return date.format();
        return null;
    }
}
