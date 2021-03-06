package net.hycrafthd.logging_util.plugin;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import net.hycrafthd.logging_util.LoggingUtil;

@Plugin(name = "SecretHider", category = PatternConverter.CATEGORY)
@ConverterKeys({ "hide" })
public class SecretHider extends LogEventPatternConverter {
	
	public static SecretHider newInstance(String[] options) {
		return new SecretHider("Secret Hider", Thread.currentThread().getName());
	}
	
	private SecretHider(String name, String style) {
		super(name, style);
	}
	
	@Override
	public void format(LogEvent event, StringBuilder toAppendTo) {
		String message = event.getMessage().getFormattedMessage();
		
		for (final String remove : LoggingUtil.REMOVE_FROM_LOG) {
			message = message.replace(remove, "xxxxxxxxxxxxxxxxxxxxxxx");
		}
		
		toAppendTo.append(message);
	}
	
}