package com.bingo.bootjudge.framework;

import java.net.URI;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class CustomConfigurationFactory extends ConfigurationFactory {

	
	static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
		String log4jPath = "c:/log/";
	 
		builder.setConfigurationName(name);
		builder.setStatusLevel(Level.INFO);
		builder.setPackages(CustomConfigurationFactory.class.getPackage().getName());
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
				ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern",
				"%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level --- [%21t]   %-72C  :  %msg%n"));
		builder.add(appenderBuilder);
		
		AppenderComponentBuilder appenderBuilderFile = builder.newAppender("rolling", "RollingFile");
		appenderBuilderFile .addAttribute("fileName", log4jPath + "/judge.html")
		   .addAttribute("filePattern", log4jPath + "/judge_%i.html");
		
		appenderBuilderFile.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "500KB"));
		
		appenderBuilderFile.add(builder.newLayout("HtmlPrintLayout").addAttribute("title", "Log4j Logger Html"));
		
		appenderBuilderFile.addComponent(builder.newComponent("DefaultRolloverStrategy").addAttribute("max", "10"));
		builder.add(appenderBuilderFile);
		builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("Stdout"))
				.add(builder.newAppenderRef("rolling")));
		return builder.build();
	}

	@Override
	public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
		return getConfiguration(loggerContext, source.toString(), null);
	}

	@Override
	public Configuration getConfiguration(final LoggerContext loggerContext, final String name,
			final URI configLocation) {
		ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
		return createConfiguration(name, builder);
	}

	@Override
	protected String[] getSupportedTypes() {
		return new String[] { "*" };
	}
}