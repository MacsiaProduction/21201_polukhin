package ru.nsu.fit.m_polukhin;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.m_polukhin.exceptions.DuParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testGetOptions() throws DuParseException {
        String[] args = {"-depth", "10", "-L", "-limit", "10"};
        JduOptions options = Parser.getOptions(args);
        assertNotNull(options);
        assertEquals(10, options.depth());
        assertTrue(options.followSymLinks());
        assertEquals(10, options.limit());
        assertEquals(Path.of("."), options.rootPath());
    }

    @Test
    void testGetOptionsWithPath() throws DuParseException, IOException {
        Path path = Files.createTempFile("temp", "tmp");
        String[] args = {path.toString(), "-depth", "10", "-L", "-limit", "10"};
        JduOptions options = Parser.getOptions(args);
        assertNotNull(options);
        assertEquals(10, options.depth());
        assertTrue(options.followSymLinks());
        assertEquals(10, options.limit());
        assertEquals(path, options.rootPath());
    }

    @Test
    void testGetOptionsInvalidNumberOfArguments() {
        String[] args = {"/home/user/test", "/home/user/test2", "-depth", "10", "-L", "-limit", "10"};
        assertThrows(DuParseException.class, () -> Parser.getOptions(args));
    }

    @Test
    void testGetOptionsInvalidArgumentValue() {
        String[] args = {"-depth", "-1", "-L", "-limit", "10"};
        assertThrows(DuParseException.class, () -> Parser.getOptions(args));
    }

    @Test
    void testGetOptionsInvalidPath() {
        String[] args = {"/invalid/path", "-depth", "10", "-L", "-limit", "10"};
        assertThrows(DuParseException.class, () -> Parser.getOptions(args));
    }

    @Test
    void testParseUnsignedInt() throws DuParseException {
        int expected = 10;
        int actual = Parser.parseUnsignedInt("10");
        assertEquals(expected, actual);
    }

    @Test
    void testParseUnsignedIntNegative() {
        assertThrows(DuParseException.class, () -> Parser.parseUnsignedInt("-1"));
    }

    @Test
    void testParsePathNotExists() {
        assertThrows(DuParseException.class, () -> Parser.parsePath("/invalid/path"));
    }
}