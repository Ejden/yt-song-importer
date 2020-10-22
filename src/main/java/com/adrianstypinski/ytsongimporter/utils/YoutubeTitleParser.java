package com.adrianstypinski.ytsongimporter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeTitleParser {

    public static String parseTitle(String title) {
        Pattern pattern = Pattern.compile("([\\w '&,\\d.]+)( *—|-|– *)([\\w\\d '&\"]+)");

        Matcher matcher = pattern.matcher(title);

        boolean matchFound = matcher.find();

        if (matchFound) {
            String group1 = matcher.group(1).trim();
            String group2 = matcher.group(3).trim();

            return String.format("%s %s", group1, group2).replaceAll("&", "").replaceAll(" ", "+").replaceAll("\"", "");
        } else {
            return null;
        }
    }
}
