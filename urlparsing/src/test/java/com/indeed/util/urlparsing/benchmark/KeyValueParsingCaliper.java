package com.indeed.util.urlparsing.benchmark;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.indeed.util.urlparsing.ParseUtils;
import com.indeed.util.urlparsing.QueryStringParser;
import com.indeed.util.urlparsing.QueryStringParserCallback;

/**
 * To run ...
 * <p/>
 * INSTALL CALIPER VERSION
 * git clone https://code.google.com/p/caliper/
 * git checkout 9788e7e
 * cd caliper
 * sed -i 's/SNAPSHOT/9788e7e/' pom.xml
 * mvn clean install
 * <p/>
 * RUN THROUGH MAVEN
 * cd $indeedeng/util/urlparsing
 * mvn clean package
 * mvn exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.google.caliper.runner.CaliperMain" -Dexec.args="com.indeed.util.urlparsing.benchmark.KeyValueParsingCaliper"
 * <p/>
 * EXAMPLE RESULT URL
 * https://microbenchmarks.appspot.com/runs/fa5246e1-aef7-4b3b-b6b3-ffc836897555#r:scenario.benchmarkSpec.methodName
 */
public final class KeyValueParsingCaliper {
    @Param({
            "uid=165faaa89a8efa57&q=java&l=Austin%2C%20TX&totCnt=10",
            "uid=165aaeaa87b8caf8&q=&l=Austin%2C%20TX&totCnt=8"
    })
    String logEntry;
    private final QueryStringParserCallback<JobSearchLogRecord> callback = IndeedKeyValueParser.createCallback();
    private final StringSplitKeyValueParser ssParser = new StringSplitKeyValueParser();

    @Benchmark
    public int IndeedKeyValue(int reps) {
        final String logEntry = this.logEntry;
        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            final JobSearchLogRecord record = new JobSearchLogRecord();
            QueryStringParser.parseQueryString(logEntry, callback, record);
            dummy += record.getNumResults();
        }
        return dummy;
    }

    @Benchmark
    public int StringSplitKeyValue(int reps) {
        final String logEntry = this.logEntry;
        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            final String totCnt = ssParser.parse(logEntry).get("totCnt");
            dummy |= ParseUtils.parseInt(totCnt, 0, totCnt.length()); // use ours to minimize surface area of test
        }
        return dummy;
    }
}
