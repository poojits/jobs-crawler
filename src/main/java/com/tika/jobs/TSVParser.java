package org.apache.tika.parser.tsv;

import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


public class TSVParser extends AbstractParser {

    private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.text("tab-separated-values"));
    public static final String TSV_MIME_TYPE = "text/tab-separated-values";

    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }

    private static final ServiceLoader LOADER = new ServiceLoader(TSVParser.class.getClassLoader());

    public void parse(
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
            throws IOException, SAXException, TikaException {

        // Automatically detect the character encoding
        AutoDetectReader reader = new AutoDetectReader(
                new CloseShieldInputStream(stream), metadata,
                context.get(ServiceLoader.class, LOADER));
        try {
            MediaType type = new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"));
            metadata.set(Metadata.CONTENT_TYPE, type.toString());
            metadata.set(Metadata.CONTENT_ENCODING, Charset.forName("UTF-8").name());
            ArrayList<String> headings = new ArrayList(Arrays.asList("postedDate","location","department","title","salary","start","duration","jobtype","applications","company","contactPerson","phoneNumber","faxNumber","location","latitude","longitude","firstSeenDate","url","lastSeenDate"));
            XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
            xhtml.startDocument();
            xhtml.startElement("table");
            xhtml.startElement("tr");
            for(int i=0;i<headings.size();i++){
                xhtml.startElement("th");
                xhtml.characters(headings.get(i));
                xhtml.endElement("th");
            }
            xhtml.endElement("tr");
            while(reader.ready()){
                xhtml.startElement("tr");
                String line = reader.readLine();
                String[] cols = line.split("\t");
                for(int i=0;i<cols.length;i++){
                    xhtml.startElement("td");
                    xhtml.characters(cols[i]);
                    xhtml.endElement("td");
                }
                xhtml.endElement("tr");
            }
            xhtml.endElement("table");
            xhtml.endDocument();
        } finally {
            reader.close();
        }
    }
}