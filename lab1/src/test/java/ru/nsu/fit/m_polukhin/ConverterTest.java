package ru.nsu.fit.m_polukhin;

import org.junit.Test;
import ru.nsu.fit.m_polukhin.core.DuTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest extends DuTest {
    @Test
    public void testConvert() {
        assertEquals("[1Gib]", Converter.convert(1073741824L));
        assertEquals("[1Mib]", Converter.convert(1048576L));
        assertEquals("[1Kib]", Converter.convert(1024L));
        assertEquals("[512B]", Converter.convert(512L));
    }
}
