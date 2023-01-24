package hr.algebra.codenames.xml.jaxb;

import hr.algebra.codenames.xml.model.XmlTurnListWrapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

public class XMLConverter {
    private XMLConverter(){}
    public static String ConvertToXml(XmlTurnListWrapper xmlTurnListWrapper) throws Exception {
        Serializer serializer = new Persister();
        StringWriter writer = new StringWriter();

        serializer.write(xmlTurnListWrapper, writer);
        return writer.toString();
    }
}
