package com.pav.avdonin.util;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CommonFunctionsTest {

    @Test
    public void getCurrentTime() {
        String currentTime = new CommonFunctions().getCurrentTime();
        Pattern p = Pattern.compile("\\d\\d\\.\\d\\d\\s\\d\\d\\:\\d\\d");
        Matcher m = p.matcher(currentTime);
        System.out.println(m.matches());
        assert (m.matches());


    }

    @Test
    public void getCurrentTimeWithSeconds() {

        String currentTime = new CommonFunctions().getCurrentTimeWithSeconds();
        Pattern p = Pattern.compile("\\d\\d\\.\\d\\d\\s\\d\\d\\:\\d\\d\\:\\d\\d");
        Matcher m = p.matcher(currentTime);
        System.out.println(m.matches());
        assert (m.matches());

    }

    @Test
    public void closeSocketAndStreams() {
    }

    @Test
    public void getDayOfWeek() {
        CommonFunctions commonFunctions = new CommonFunctions();
        List<String> dayOfWeek = Arrays.asList(new String [] {"понедельник","вторник","среда","четверг","пятница", "суббота", "воскресенье"});
        assert (dayOfWeek.contains(new CommonFunctions().getDayOfWeek()));
    }
}
