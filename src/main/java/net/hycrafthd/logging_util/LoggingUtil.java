package net.hycrafthd.logging_util;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LoggerContext;

public class LoggingUtil {
	
	public static final Set<String> REMOVE_FROM_LOG = new HashSet<>();
	
	private static Map<org.apache.logging.log4j.core.Logger, Level> LOGGER_ORIGINAL_LEVELS;
	
	public static void redirectPrintStreams(Logger logger) {
		System.setErr(new LoggingPrintStream(logger, Level.ERROR, MarkerManager.getMarker("STDERR"), System.err));
		System.setOut(new LoggingPrintStream(logger, Level.INFO, MarkerManager.getMarker("STDOUT"), System.out));
		
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			logger.fatal("A fatal exception occured :", throwable);
		});
	}
	
	public static void addRemoveFromLog(String removeFromLog) {
		REMOVE_FROM_LOG.add(removeFromLog);
	}
	
	public static void disableLogging() {
		final LoggerContext context = (LoggerContext) LogManager.getContext(false);
		LOGGER_ORIGINAL_LEVELS = new HashMap<>();
		context.getLoggers().forEach(logger -> {
			LOGGER_ORIGINAL_LEVELS.put(logger, logger.getLevel());
			logger.setLevel(Level.OFF);
		});
	}
	
	public static void enableLogging() {
		if (LOGGER_ORIGINAL_LEVELS != null) {
			LOGGER_ORIGINAL_LEVELS.forEach((logger, level) -> {
				logger.setLevel(level);
			});
		}
	}
	
	private static class LoggingPrintStream extends PrintStream {
		
		private final Logger logger;
		private final Level level;
		private final Marker marker;
		
		public LoggingPrintStream(Logger logger, Level level, Marker marker, PrintStream printStream) {
			super(printStream);
			this.logger = logger;
			this.level = level;
			this.marker = marker;
		}
		
		private void log(String string) {
			logger.log(level, marker, "[{}] {}", Thread.currentThread().getStackTrace()[3], string);
		}
		
		// Catch most common methods for print stream
		
		@Override
		public void println(String value) {
			log(value);
		}
		
		@Override
		public void println(Object value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(boolean value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(char value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(int value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(long value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(float value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(double value) {
			log(String.valueOf(value));
		}
		
		@Override
		public void println(char[] value) {
			log(String.valueOf(value));
		}
	}
}