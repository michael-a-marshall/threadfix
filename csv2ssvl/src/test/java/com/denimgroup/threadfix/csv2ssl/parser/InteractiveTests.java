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
package com.denimgroup.threadfix.csv2ssl.parser;

import com.denimgroup.threadfix.csv2ssl.Main;
import com.denimgroup.threadfix.csv2ssl.ResourceLoader;
import com.denimgroup.threadfix.csv2ssl.util.InteractionUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by mcollins on 1/21/15.
 */
public class InteractiveTests {

    private void testDialog(String dialog) {
        InteractionUtils.reader = new BufferedReader(new StringReader(dialog));

        String s = Main.doParsing(new String[]{});

        assert s.contains("<Vulnerability") : "Didn't have any vulnerabilities.";
    }

    @Test
    public void testBasicDialog() {

        // define inputs
        String dialog =
                "n\n" +
                "CWE,url,Parameter,LongDescription,NativeID,Source\n" +
                "y\n" +
                "n\n" +
                ResourceLoader.getFilePath("basic.csv") + "\n" +
                ResourceLoader.getFilePath("out.ssvl") + "\n";

        testDialog(dialog);
    }

    @Test
    public void testWithColumnConfiguration() {

        // define inputs
        String dialog =
                "n\n" +
                "1,2,3,4,5,6\n" +
                "y\n" +
                "y\n" +
                "skip\n" +
                "1\n" +
                "6\n" +
                "2\n" +
                "3\n" +
                "5\n" +
                "4\n" +
                "skip\n" +
                "skip\n" +
                "n\n" +
                ResourceLoader.getFilePath("basic.csv") + "\n" +
                "stdout\n";

        testDialog(dialog);
    }

    @Test
    public void testWithHeaderInFile() {

        String dialog =
                "y\n" +
                ResourceLoader.getFilePath("withHeaderLine.csv") + "\n" +
                "y\n" +
                "n\n" +
                "stdout\n";

        testDialog(dialog);
    }

    @Test
    public void testWithDifferentHeadersInFile() {
        String dialog =
                "y\n" +
                ResourceLoader.getFilePath("withDifferentHeaderLine.csv") + "\n" +
                "y\n" +
                "y\n" +
                "skip\n" +
                "VulnType\n" +
                "Scanner\n" +
                "Location\n" +
                "Injection Point\n" +
                "ID\n" +
                "Text\n" +
                "skip\n" +
                "skip\n" +
                "n\n" +
                "stdout\n";

        testDialog(dialog);



    }

}
