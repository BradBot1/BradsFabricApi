package com.bb1.fabric.bfapi.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ExceptionWrapper<I, R> {
	
	public static <I> void execute(@NotNull ExceptionWrapperWithoutReturn<I> wrapper) {
		execute(null, wrapper);
	}
	
	public static <I> void execute(@Nullable I input, @NotNull ExceptionWrapperWithoutReturn<I> wrapper) {
		try {
			wrapper.run(input);
		} catch (Throwable e) { }
	}
	
	public static <I> void execute(@NotNull ExceptionWrapperWithoutReturn<I> wrapper, @NotNull Consumer<Throwable> consumer) {
		execute(null, wrapper, consumer);
	}
	
	public static <I> void execute(@Nullable I input, @NotNull ExceptionWrapperWithoutReturn<I> wrapper, @NotNull Consumer<Throwable> consumer) {
		try {
			wrapper.run(input);
		} catch (Throwable e) {
			consumer.accept(e);
		}
	}
	
	public static<R> @Nullable R executeWithReturn(@NotNull ExceptionWrapper<Void, @Nullable R> wrapper) {
		return executeWithReturn(null, wrapper);
	}
	
	public static <I, R> @Nullable R executeWithReturn(@Nullable I input, @NotNull ExceptionWrapper<@Nullable I, @Nullable R> wrapper) {
		try {
			return wrapper.run(input);
		} catch (Throwable e) {
			return null;
		}
	}
	
	public static<R> @Nullable R executeWithReturn(@NotNull ExceptionWrapper<Void, @Nullable R> wrapper, @NotNull Consumer<Throwable> consumer) {
		return executeWithReturn(null, wrapper, consumer);
	}
	
	public static <I, R> @Nullable R executeWithReturn(@Nullable I input, @NotNull ExceptionWrapper<@Nullable I, @Nullable R> wrapper, @NotNull Consumer<Throwable> consumer) {
		try {
			return wrapper.run(input);
		} catch (Throwable e) {
			consumer.accept(e);
			return null;
		}
	}
	
	public static <I, R> @Nullable R executeWithReturn(@Nullable I input, @NotNull R defaultValue, @NotNull ExceptionWrapper<@Nullable I, @Nullable R> wrapper) {
		try {
			return wrapper.run(input);
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static <I, R> @Nullable R executeWithReturn(@Nullable I input, @NotNull R defaultValue, @NotNull ExceptionWrapper<@Nullable I, @Nullable R> wrapper, @NotNull Consumer<Throwable> consumer) {
		try {
			return wrapper.run(input);
		} catch (Throwable e) {
			consumer.accept(e);
			return defaultValue;
		}
	}
	
	public static @NotNull ExceptionWrapperWithoutReturn<@Nullable Void> wrap(@NotNull Runnable runnable) { return (i)->{ runnable.run(); }; }
	
	public static <I> @NotNull ExceptionWrapperWithoutReturn<@Nullable I> wrap(@NotNull Consumer<I> consumer) { return (i)->{ consumer.accept(i); }; }
	
	public static <I, R> @NotNull ExceptionWrapper<@Nullable I, @Nullable R> wrap(@NotNull Function<I, R> func) { return (i)->{ return func.apply(i); }; }
	
	public R run(@NotNull I input) throws Throwable;
	
	public static interface ExceptionWrapperWithoutReturn<I> extends ExceptionWrapper<I, Void> {
		
		public default Void run(@NotNull I input) throws Throwable {
			runWithReturn(input);
			return null;
		}
		
		public void runWithReturn(@NotNull I input) throws Throwable;
		
	}
	
}
