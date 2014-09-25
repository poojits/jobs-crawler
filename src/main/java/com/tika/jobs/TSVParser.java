package com.tika.jobs;

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
import java.util.Collections;
import java.util.Set;

public class TSVParser extends AbstractParser {

    private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.text("tab-separated-values"));
    public static final String TSV_MIME_TYPE = "text/tab-separated-values";

    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }

    private static final ServiceLoader LOADER = new ServiceLoader(TSVParser.class.getClassLoader());

    public void parse(InputStream stream, ContentHandler handler,Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
        AutoDetectReader reader = new AutoDetectReader(new CloseShieldInputStream(stream), metadata, context.get(ServiceLoader.class, LOADER));
        try {
            XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
            xhtml.startDocument();
            xhtml.startElement("p");
            char[] buffer = new char[4096];
            int n = reader.read(buffer);
            while (n != -1) {
                xhtml.characters(buffer, 0, n);
                n = reader.read(buffer);
            }
            xhtml.endElement("p");
            xhtml.endDocument();
        }
        finally {
            reader.close();
        }
    }
}
