package com.indeed.util.urlparsing.benchmark;

import java.util.Map;

/**
 * @author: preetha
 */
public interface KeyValueParser {

    public Map<String, String> parse(String log);
}
