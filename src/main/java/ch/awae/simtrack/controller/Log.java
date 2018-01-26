package ch.awae.simtrack.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Log {
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss.S");

	public static void info(String msg, Object... args) {
		log("INFO", msg + " " + StringUtils.join(args, ","));
	}

	public static void warn(String msg, Object... args) {
		log("WARN", msg + " " + StringUtils.join(args, ","));
	}

	public static void err(String msg, Object... args) {
		log("ERROR", msg + " " + StringUtils.join(args, ","));
	}

	private static void log(String level, String text) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[3];

		StringBuilder builder = new StringBuilder(1000);
		builder.append(df.format(new Date())).append(" ");
		builder.append("[").append(level).append("] ");
		builder.append("(").append(caller.getFileName()).append(":");
		builder.append(caller.getLineNumber()).append(") ");
		builder.append(text);
		System.out.println(builder.toString());
	}
}