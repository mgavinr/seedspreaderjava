package com.grogers.seedspreaderjava;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.grogers.seedspreaderjava.frontend.LanguageProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class testInstrumentedLanguageProcessor {
    @Test
    public void testAndroid1() {
        ArrayList<Integer>  arrayList = LanguageProcessor.getRowCol("r1 r2", 10, 10);
        Log.d("*&*", "The arrayList result is: " + arrayList.toString());
        assertEquals(arrayList.size(), 1);
    }
}