package org.writer.exception.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.CsvUnexpectedException;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvErrorHandlerTest {
    private CsvErrorHandler errorHandler;
    private TestLogger testLogger;

    @BeforeEach
    public void setUp() {
        errorHandler = new CsvErrorHandler();
        testLogger = TestLoggerFactory.getTestLogger(CsvErrorHandler.class);
        TestLoggerFactory.clear();
    }

    @Test
    public void testHandleCsvFileWriteException() {
        String message = "Error occurred";
        var ex = new Exception("Test exception");

        assertThrows(CsvFileWriteException.class,
                () -> errorHandler.handleError(message, ex, CsvFileWriteException.class),
                "Expected a CsvFileWriteException to be thrown");

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size(), "Expected one logging event");

        LoggingEvent actualEvent = loggingEvents.get(0);
        assertEquals("ERROR", actualEvent.getLevel()
                .toString(), "Log level should be ERROR");
        assertEquals(message, actualEvent.getMessage(), "Log message should match");
    }

    @Test
    public void testHandleCsvReflectionException() {
        String message = "Error occurred";
        var ex = new Exception("Test exception");

        assertThrows(CsvReflectionException.class,
                () -> errorHandler.handleError(message, ex, CsvReflectionException.class),
                "Expected a CsvReflectionException to be thrown");

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size(), "Expected one logging event");

        LoggingEvent actualEvent = loggingEvents.get(0);
        assertEquals("ERROR", actualEvent.getLevel()
                .toString(), "Log level should be ERROR");
        assertEquals(message, actualEvent.getMessage(), "Log message should match");
    }

    @Test
    public void testHandleCsvUnexpectedException() {
        String message = "Error occurred";
        var ex = new Exception("Test exception");

        assertThrows(CsvUnexpectedException.class,
                () -> errorHandler.handleError(message, ex, CsvUnexpectedException.class),
                "Expected a CsvUnexpectedException to be thrown");

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size(), "Expected one logging event");

        LoggingEvent actualEvent = loggingEvents.get(0);
        assertEquals("ERROR", actualEvent.getLevel()
                .toString(), "Log level should be ERROR");
        assertEquals(message, actualEvent.getMessage(), "Log message should match");
    }

    @Test
    public void testHandleRuntimeException() {
        String message = "Error occurred";
        var ex = new Exception("Test exception");

        assertThrows(RuntimeException.class, () -> errorHandler.handleError(message, ex, RuntimeException.class),
                "Expected a RuntimeException to be thrown");

        List<LoggingEvent> loggingEvents = testLogger.getLoggingEvents();
        assertEquals(1, loggingEvents.size(), "Expected one logging event");

        LoggingEvent actualEvent = loggingEvents.get(0);
        assertEquals("ERROR", actualEvent.getLevel()
                .toString(), "Log level should be ERROR");
        assertEquals(message, actualEvent.getMessage(), "Log message should match");
    }
}