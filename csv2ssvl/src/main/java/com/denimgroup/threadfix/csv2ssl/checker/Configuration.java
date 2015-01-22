////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2015 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.csv2ssl.checker;

import com.denimgroup.threadfix.csv2ssl.parser.ArgumentParser;
import com.denimgroup.threadfix.csv2ssl.parser.FormatParser;
import com.denimgroup.threadfix.csv2ssl.util.Option;
import com.denimgroup.threadfix.csv2ssl.util.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static com.denimgroup.threadfix.csv2ssl.util.CollectionUtils.newMap;

/**
 * Created by mcollins on 1/21/15.
 */
public class Configuration {

    public static enum State {
        VALID, NEEDS_HEADERS, NEEDS_INPUT_FILE, NEEDS_OUTPUT_FILE
    }

    public static final Configuration CONFIG = new Configuration();

    public String[] headers;

    public boolean useStandardOut = false, shouldSkipFirstLine = false;

    public File csvFile, outputFile;

    public Map<String, String> headerMap = getBasicHeaderMap();

    private Map<String, String> getBasicHeaderMap() {
        Map<String, String> returnMap = newMap();

        for (String headerName : Strings.HEADER_NAMES) {
            returnMap.put(headerName, headerName);
        }

        return returnMap;
    }

    public static State getCurrentState() {
        if (CONFIG.headers == null) {
            return State.NEEDS_HEADERS;
        } else if (CONFIG.csvFile == null) {
            return State.NEEDS_INPUT_FILE;
        } else if (CONFIG.outputFile == null && !CONFIG.useStandardOut) {
            return State.NEEDS_OUTPUT_FILE;
        } else {
            return State.VALID;
        }
    }

    public static void writeToFile(File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            toProperties().store(outputStream, "Saving contents.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeInternal(File outputFile) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);

            toProperties().store(outputStream, "Saving contents.");

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Properties toProperties() {

        Properties properties = new Properties();

        StringBuilder builder = new StringBuilder("");

        if (CONFIG.headers != null) {
            for (String header : CONFIG.headers) {
                builder.append(header).append(",");
            }
        }

        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }

        properties.setProperty("headers", builder.toString());

        properties.setProperty("csvFile", CONFIG.csvFile == null ? "" : CONFIG.csvFile.getAbsolutePath());
        properties.setProperty("outputFile", CONFIG.outputFile  == null ? "" : CONFIG.outputFile.getAbsolutePath());

        properties.setProperty("shouldSkipFirstLine", String.valueOf(CONFIG.shouldSkipFirstLine));
        properties.setProperty("useStandardOut", String.valueOf(CONFIG.useStandardOut));

        return properties;
    }

    // TODO this is long
    public static void setFromArguments(String[] args) {

        Option<String> configFile = ArgumentParser.parseConfigFileName(args);

        if (configFile.isValid()) {
            loadFromFile(configFile.getValue());
        }

        Option<String[]> formatString = FormatParser.getHeaders(args);

        if (formatString.isValid()) {
            CONFIG.headers = formatString.getValue();
        }

        Option<String> maybeCsvFile = ArgumentParser.parseSourceFileName(args);

        if (maybeCsvFile.isValid()) {
            String csvFileName = maybeCsvFile.getValue();

            File csvFile = new File(csvFileName);
            if (!csvFile.exists()) {
                System.out.println("CSV File '" + csvFileName + "' was not found.");
            } else if (!csvFile.isFile()) {
                System.out.println("CSV File '" + csvFileName + "' was not a regular file.");
            } else {
                CONFIG.csvFile = csvFile;
            }
        }

        Option<String> maybeTargetFile = ArgumentParser.parseTargetFileName(args);

        if (maybeTargetFile.isValid()) {
            String targetFileName = maybeTargetFile.getValue();

            File file = new File(targetFileName);

            if (file.exists()) {
                System.out.println("File '" + targetFileName + "' already existed, please specify a different path.");
            }
        }
    }

    private static void loadFromFile(String value) {

        File file = new File("config.properties");

        if (file.exists() && file.isFile()) {
            Properties properties = new Properties();

            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            loadFromProperties(properties);
        }
    }

    private static void loadFromProperties(Properties properties) {

        String headersProperty = properties.getProperty("headers");

        if (headersProperty != null) {
            CONFIG.headers = headersProperty.split(",");
        }

        String csvFileLocation = properties.getProperty("csvFile");
        if (csvFileLocation != null) {
            File file = new File(csvFileLocation);
            if (file.exists() && file.isFile()) {
                CONFIG.csvFile = file;
            }
        }

        String outputFileLocation = properties.getProperty("outputFile");
        if (outputFileLocation != null) {
            File file = new File(outputFileLocation);
            if (file.exists() && file.isFile()) {
                CONFIG.outputFile = file;
            }
        }

        CONFIG.useStandardOut = "true".equalsIgnoreCase(properties.getProperty("useStandardOut"));
        CONFIG.shouldSkipFirstLine = "true".equalsIgnoreCase(properties.getProperty("shouldSkipFirstLine"));
    }

}
