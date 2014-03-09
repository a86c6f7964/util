package com.indeed.util.urlparsing.benchmark;

import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.api.SkipThisScenarioException;
import com.indeed.util.urlparsing.ParseUtils;

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
 * mvn exec:java -Dexec.classpathScope="test" -Dexec.mainClass="com.google.caliper.runner.CaliperMain" -Dexec.args="com.indeed.util.urlparsing.benchmark.NumberParsingCaliper"
 * <p/>
 * EXAMPLE RESULT URL
 * https://microbenchmarks.appspot.com/runs/2e180fa8-4504-45a4-845c-cc39a5ecb7e4#r:scenario.benchmarkSpec.methodName
 */
public final class NumberParsingCaliper {
    @Param({
            "0", "3156169", "-4564565", "5555555", "-55555556", "359689755",
            "0.", "315.6169", "-4564.565", "555.5555", "-55555.556", "359689.755"
    })
    String number;

    @Benchmark
    public int JavaInt(int reps) {
        if (number.contains(".")) {
            throw new SkipThisScenarioException();
        }

        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            dummy += Integer.parseInt(number, 10);
        }
        return dummy;
    }

    @Benchmark
    public int IndeedUnsignedInt(int reps) {
        if (number.charAt(0) == '-' || number.contains(".")) {
            throw new SkipThisScenarioException();
        }

        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            dummy += ParseUtils.parseUnsignedInt(number, 0, number.length());
        }
        return dummy;
    }

    @Benchmark
    public int IndeedSignedInt(int reps) {
        if (number.contains(".")) {
            throw new SkipThisScenarioException();
        }

        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            dummy += ParseUtils.parseSignedInt(number, 0, number.length());
        }
        return dummy;
    }

    @Benchmark
    public double JavaFloat(int reps) {
        if (!number.contains(".")) {
            throw new SkipThisScenarioException();
        }

        double dummy = 0;
        for (int i = 0; i < reps; i++) {
            dummy += Float.parseFloat(number);
        }
        return dummy;
    }

    @Benchmark
    public double IndeedFloat(int reps) {
        if (!number.contains(".")) {
            throw new SkipThisScenarioException();
        }

        double dummy = 0;
        for (int i = 0; i < reps; i++) {
            dummy += ParseUtils.parseFloat(number, 0, number.length());
        }
        return dummy;
    }
}
