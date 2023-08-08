package com.grogers.seedspreaderjava;
import com.grogers.seedspreaderjava.frontend.LanguageProcessor;
// android
import android.util.Log;
// java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
// test
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

@Retention(RetentionPolicy.RUNTIME)
@interface TestDescription {
    String value();
}

public class testLanguageProcessor {

    @Test
    @TestDescription("This test checks the return from enter seed or event row/cols")
    public void testJava1() {
        int maxRow = 10;
        int maxCol = 10;
        ArrayList<Integer>  arrayList = LanguageProcessor.getRowCol("r1 r2", maxRow, maxCol);
        Log.d("*&*", "The arrayList result is: " + arrayList.toString());
        Assert.assertEquals(arrayList.size(), 2 + (maxCol * 2));
        Assert.assertTrue("r1 is not present", arrayList.contains(-1));
        Assert.assertTrue("r2 is not present", arrayList.contains(-2));
        Assert.assertFalse("r3 is not present", arrayList.contains(-3));
    }
}