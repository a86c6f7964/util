package com.indeed.util.urlparsing.benchmark;

import com.indeed.util.urlparsing.ParseUtils;
import com.indeed.util.urlparsing.QueryStringParser;
import com.indeed.util.urlparsing.QueryStringParserCallback;
import com.indeed.util.urlparsing.QueryStringParserCallbackBuilder;

import java.util.Map;

/**
 * @author: preetha
 */
public class IndeedKeyValueParser implements  KeyValueParser {
    private static final QueryStringParserCallback<JobSearchLogRecord> uidParser = new QueryStringParserCallback<JobSearchLogRecord>() {
        @Override
        public void parseKeyValuePair(String urlParams, int keyStart, int keyEnd, int valueStart, int valueEnd, JobSearchLogRecord storage) {
            storage.getUid().append(urlParams, valueStart, valueEnd);
        }
    };

    private static final QueryStringParserCallback<JobSearchLogRecord> timestampParser = new QueryStringParserCallback<JobSearchLogRecord>() {
        @Override
        public void parseKeyValuePair(String urlParams, int keyStart, int keyEnd, int valueStart, int valueEnd, JobSearchLogRecord storage) {
            storage.setTimestamp(ParseUtils.parseTimestampFromUIDString(urlParams, valueStart, valueEnd));
        }
    };

    private static final QueryStringParserCallback<JobSearchLogRecord> queryParser = new QueryStringParserCallback<JobSearchLogRecord>() {
        @Override
        public void parseKeyValuePair(String urlParams, int keyStart, int keyEnd, int valueStart, int valueEnd, JobSearchLogRecord storage) {
            ParseUtils.urlDecodeInto(urlParams, valueStart, valueEnd, storage.getQuery());
        }
    };

    private static final QueryStringParserCallback<JobSearchLogRecord> locationParser = new QueryStringParserCallback<JobSearchLogRecord>() {
        @Override
        public void parseKeyValuePair(String urlParams, int keyStart, int keyEnd, int valueStart, int valueEnd, JobSearchLogRecord storage) {
            ParseUtils.urlDecodeInto(urlParams, valueStart, valueEnd, storage.getLocation());
        }
    };

    private static final QueryStringParserCallback<JobSearchLogRecord> intValueParser = new QueryStringParserCallback<JobSearchLogRecord>() {
        @Override
        public void parseKeyValuePair(String urlParams, int keyStart, int keyEnd, int valueStart, int valueEnd, JobSearchLogRecord storage) {
            storage.setNumResults(ParseUtils.parseUnsignedInt(urlParams, valueStart, valueEnd));
        }
    };


    @Override
    public Map<String, String> parse(String logentry) {
        final QueryStringParserCallback<JobSearchLogRecord> jobSearchLogRecordParser = createCallback();
        final JobSearchLogRecord record = new JobSearchLogRecord();
        QueryStringParser.parseQueryString(logentry, jobSearchLogRecordParser, record);
        return null;
    }

    public static QueryStringParserCallback<JobSearchLogRecord> createCallback() {
        QueryStringParserCallbackBuilder<JobSearchLogRecord> builder = new QueryStringParserCallbackBuilder<JobSearchLogRecord>();
        builder.addCallback("uid", uidParser);
        builder.addCallback("uid", timestampParser);
        builder.addCallback("q",  queryParser);
        builder.addCallback("l", locationParser);
        builder.addCallback("totCnt", intValueParser);

        return builder.buildCallback();
    }
}
