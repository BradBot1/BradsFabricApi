package com.bb1.fabric.bfapi.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.Constants;
import com.bb1.fabric.bfapi.registery.BFAPIRegistry;
import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;

import net.minecraft.util.Identifier;

public interface EventListener extends IRegisterable {
	
	static final Logger LOGGER = Constants.createSubLogger("EventListener");
	
	public default void register(@Nullable Identifier name) {
		final Object ref = this;
		for (Method method : getClass().getMethods()) {
			EventHandler handler = method.getAnnotation(EventHandler.class);
			if (handler!=null) {
				method.setAccessible(true);
				Event<?> event = BFAPIRegistry.EVENTS.get(new Identifier(handler.eventIdentifier()));
				if (event!=null) {
					if (handler.decomposeArguments()) {
						event.addHandler((input)->{
							ExceptionWrapper.execute(input, (i)->{
								method.invoke(ref, i.getAllRaw());
							}, (e)->{
								LOGGER.error("A '"+e.getClass().getSimpleName()+"' occured while trying to invoke an event hanlder for the event '"+handler.eventIdentifier()+"'");
								if (handler.required()) {
									throw new IllegalStateException("Cannot continue as the event '"+handler.eventIdentifier()+"' is desginated as required");
								}
							});
						});
					} else {
						event.addHandler((input)->{
							ExceptionWrapper.execute(input, (i)->{
								method.invoke(ref, i);
							}, (e)->{
								LOGGER.error("A '"+e.getClass().getSimpleName()+"' occured while trying to invoke an event hanlder for the event '"+handler.eventIdentifier()+"'");
								if (handler.required()) {
									throw new IllegalStateException("Cannot continue as the event '"+handler.eventIdentifier()+"' is desginated as required");
								}
							});
						});
					}
				} else {
					if (handler.logOnFailedBinding()) {
						LOGGER.warn("Failed to bind to the event '"+handler.eventIdentifier()+"'! Is it not present?");
					}
					if (handler.required()) {
						throw new IllegalStateException("Cannot continue as the event '"+handler.eventIdentifier()+"' is desginated as required");
					}
				}
			}
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface EventHandler {
		
		public String eventIdentifier();
		
		public boolean required() default false;
		
		public boolean logOnFailedBinding() default true;
		
		public boolean decomposeArguments() default false;
		
	}
	
}
