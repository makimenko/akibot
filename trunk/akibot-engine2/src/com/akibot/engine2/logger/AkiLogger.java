package com.akibot.engine2.logger;

import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

/**
 * Extended Logger interface with convenience methods for the MSG custom log
 * level.
 */
public final class AkiLogger extends ExtendedLoggerWrapper {
	private static final long serialVersionUID = 3473427601231L;
	private final ExtendedLoggerWrapper logger;

	private static final String FQCN = AkiLogger.class.getName();
	private static final Level MSG = Level.forName("MSG", 800);

	private AkiLogger(final Logger logger) {
		super((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());
		this.logger = this;
	}

	/**
	 * Returns a custom Logger with the name of the calling class.
	 * 
	 * @return The custom Logger for the calling class.
	 */
	public static AkiLogger create() {
		final Logger wrapped = LogManager.getLogger();
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger using the fully qualified name of the Class as
	 * the Logger name.
	 * 
	 * @param loggerName
	 *            The Class whose name should be used as the Logger name. If
	 *            null it will default to the calling class.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final Class<?> loggerName) {
		final Logger wrapped = LogManager.getLogger(loggerName);
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger using the fully qualified name of the Class as
	 * the Logger name.
	 * 
	 * @param loggerName
	 *            The Class whose name should be used as the Logger name. If
	 *            null it will default to the calling class.
	 * @param messageFactory
	 *            The message factory is used only when creating a logger,
	 *            subsequent use does not change the logger but will log a
	 *            warning if mismatched.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final Class<?> loggerName, final MessageFactory factory) {
		final Logger wrapped = LogManager.getLogger(loggerName, factory);
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger using the fully qualified class name of the value
	 * as the Logger name.
	 * 
	 * @param value
	 *            The value whose class name should be used as the Logger name.
	 *            If null the name of the calling class will be used as the
	 *            logger name.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final Object value) {
		final Logger wrapped = LogManager.getLogger(value);
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger using the fully qualified class name of the value
	 * as the Logger name.
	 * 
	 * @param value
	 *            The value whose class name should be used as the Logger name.
	 *            If null the name of the calling class will be used as the
	 *            logger name.
	 * @param messageFactory
	 *            The message factory is used only when creating a logger,
	 *            subsequent use does not change the logger but will log a
	 *            warning if mismatched.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final Object value, final MessageFactory factory) {
		final Logger wrapped = LogManager.getLogger(value, factory);
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger with the specified name.
	 * 
	 * @param name
	 *            The logger name. If null the name of the calling class will be
	 *            used.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final String name) {
		final Logger wrapped = LogManager.getLogger(name);
		return new AkiLogger(wrapped);
	}

	/**
	 * Returns a custom Logger with the specified name.
	 * 
	 * @param name
	 *            The logger name. If null the name of the calling class will be
	 *            used.
	 * @param messageFactory
	 *            The message factory is used only when creating a logger,
	 *            subsequent use does not change the logger but will log a
	 *            warning if mismatched.
	 * @return The custom Logger.
	 */
	public static AkiLogger create(final String name, final MessageFactory factory) {
		final Logger wrapped = LogManager.getLogger(name, factory);
		return new AkiLogger(wrapped);
	}

	/**
	 * Logs a message with the specific Marker at the {@code MSG} level.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param msg
	 *            the message string to be logged
	 */
	public void msg(final Marker marker, final Message msg) {
		logger.logIfEnabled(FQCN, MSG, marker, msg, (Throwable) null);
	}

	/**
	 * Logs a message with the specific Marker at the {@code MSG} level.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param msg
	 *            the message string to be logged
	 * @param t
	 *            A Throwable or null.
	 */
	public void msg(final Marker marker, final Message msg, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, marker, msg, t);
	}

	/**
	 * Logs a message object with the {@code MSG} level.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param message
	 *            the message object to log.
	 */
	public void msg(final Marker marker, final Object message) {
		logger.logIfEnabled(FQCN, MSG, marker, message, (Throwable) null);
	}

	/**
	 * Logs a message at the {@code MSG} level including the stack trace of the
	 * {@link Throwable} {@code t} passed as parameter.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param message
	 *            the message to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public void msg(final Marker marker, final Object message, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, marker, message, t);
	}

	/**
	 * Logs a message object with the {@code MSG} level.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param message
	 *            the message object to log.
	 */
	public void msg(final Marker marker, final String message) {
		logger.logIfEnabled(FQCN, MSG, marker, message, (Throwable) null);
	}

	/**
	 * Logs a message with parameters at the {@code MSG} level.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param message
	 *            the message to log; the format depends on the message factory.
	 * @param params
	 *            parameters to the message.
	 * @see #getMessageFactory()
	 */
	public void msg(final Marker marker, final String message, final Object... params) {
		logger.logIfEnabled(FQCN, MSG, marker, message, params);
	}

	/**
	 * Logs a message at the {@code MSG} level including the stack trace of the
	 * {@link Throwable} {@code t} passed as parameter.
	 * 
	 * @param marker
	 *            the marker data specific to this log statement
	 * @param message
	 *            the message to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public void msg(final Marker marker, final String message, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, marker, message, t);
	}

	/**
	 * Logs the specified Message at the {@code MSG} level.
	 * 
	 * @param msg
	 *            the message string to be logged
	 */
	public void msg(final Message msg) {
		logger.logIfEnabled(FQCN, MSG, null, msg, (Throwable) null);
	}

	/**
	 * Logs the specified Message at the {@code MSG} level.
	 * 
	 * @param msg
	 *            the message string to be logged
	 * @param t
	 *            A Throwable or null.
	 */
	public void msg(final Message msg, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, null, msg, t);
	}

	/**
	 * Logs a message object with the {@code MSG} level.
	 * 
	 * @param message
	 *            the message object to log.
	 */
	public void msg(final Object message) {
		logger.logIfEnabled(FQCN, MSG, null, message, (Throwable) null);
	}

	/**
	 * Logs a message at the {@code MSG} level including the stack trace of the
	 * {@link Throwable} {@code t} passed as parameter.
	 * 
	 * @param message
	 *            the message to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public void msg(final Object message, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, null, message, t);
	}

	/**
	 * Logs a message object with the {@code MSG} level.
	 * 
	 * @param message
	 *            the message object to log.
	 */
	public void msg(final String message) {
		logger.logIfEnabled(FQCN, MSG, null, message, (Throwable) null);
	}

	/**
	 * Logs a message with parameters at the {@code MSG} level.
	 * 
	 * @param message
	 *            the message to log; the format depends on the message factory.
	 * @param params
	 *            parameters to the message.
	 * @see #getMessageFactory()
	 */
	public void msg(final String message, final Object... params) {
		logger.logIfEnabled(FQCN, MSG, null, message, params);
	}

	/**
	 * Logs a message at the {@code MSG} level including the stack trace of the
	 * {@link Throwable} {@code t} passed as parameter.
	 * 
	 * @param message
	 *            the message to log.
	 * @param t
	 *            the exception to log, including its stack trace.
	 */
	public void msg(final String message, final Throwable t) {
		logger.logIfEnabled(FQCN, MSG, null, message, t);
	}

	public void msg(final String componentName, final com.akibot.engine2.message.Message message) {
		String msg = "MSG / " + componentName + " / " + message.getFrom() + " / " + message.getTo() + " / " + message;
		//System.out.println(msg);
		logger.logIfEnabled(FQCN, MSG, null, msg, (Throwable) null);
	}

}
