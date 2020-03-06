package org.apache.camel.dataformat.rss;

import java.io.InputStream;
import java.io.OutputStream;

import com.rometools.rome.feed.synd.SyndFeed;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatName;
import org.apache.camel.support.ServiceSupport;
import org.apache.camel.util.ExchangeHelper;

/**
 * RSS DataFormat
 * <p/>
 * This data format supports two operations:
 * <ul>
 *   <li>marshal = from ROME SyndFeed to XML String </li>
 *   <li>unmarshal = from XML String to ROME SyndFeed </li>
 * </ul>
 * <p/>
 * Uses <a href="https://rome.dev.java.net/">ROME</a> for RSS parsing.
 * <p/>
 */
public class RssDataFormat extends ServiceSupport implements DataFormat, DataFormatName {

    @Override
    public String getDataFormatName() {
        return "rss";
    }

    public void marshal(Exchange exchange, Object body, OutputStream out) throws Exception {
        SyndFeed feed = ExchangeHelper.convertToMandatoryType(exchange, SyndFeed.class, body);
        String xml = RssConverter.feedToXml(feed);
        out.write(xml.getBytes());
    }

    public Object unmarshal(Exchange exchange, InputStream in) throws Exception {
        String xml = ExchangeHelper.convertToMandatoryType(exchange, String.class, in);
        return RssConverter.xmlToFeed(xml);
    }

    @Override
    protected void doStart() throws Exception {
        // noop
    }

    @Override
    protected void doStop() throws Exception {
        // noop
    }
}
