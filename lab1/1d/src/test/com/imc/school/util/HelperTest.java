package com.imc.school.util;

import com.imc.school.util.Helper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelperTest {
    @Test
    void test() {
        String string = Helper.appendExclamationMark("Hello");
        assertEquals(string, "Hello!");
    }
}
