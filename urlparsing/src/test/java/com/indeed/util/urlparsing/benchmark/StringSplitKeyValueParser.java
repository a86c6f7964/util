package com.indeed.util.urlparsing.benchmark;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author: preetha
 */
public class StringSplitKeyValueParser implements KeyValueParser {

    private static final Logger LOGGER = Logger.getLogger(StringSplitKeyValueParser.class);

    @Override
    public Map<String, String> parse(String log) {
        final Map<String, String> query_pairs = Maps.newHashMap();
        final String[] pairs = log.split("&");
        try {
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                final String key = URLDecoder.decode(pair.substring(0, idx), Charsets.UTF_8.name());
                final String value = URLDecoder.decode(pair.substring(idx + 1), Charsets.UTF_8.name());
                query_pairs.put(key, value);
            }
        } catch(UnsupportedEncodingException ex) {
            LOGGER.error("Unable to url decode ",ex);
        }
        return query_pairs;
    }
}
