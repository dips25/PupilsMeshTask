package com.task.pupilsmeshtask.Utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {

    public fun isBlank(s: String?): Boolean {

        if (s == null) {

            return true;
        }

        if (s.equals("")) {

            return true;
        }

        var pattern: Pattern = Pattern.compile("\\s+");
        var m: Matcher = pattern.matcher(s);

        if (m.matches()) {

            return true;
        }

        return false;

    }
}