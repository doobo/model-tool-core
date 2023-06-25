package com.github.doobo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用的Builder模式构建器
 */
public class DoBuilder<T> {

	private final Supplier<T> instance;

	private final List<Consumer<T>> modifiers = new ArrayList<>();

	public DoBuilder(Supplier<T> instance) {
		this.instance = instance;
	}

	public static <T> DoBuilder<T> of(Supplier<T> ins) {
		return new DoBuilder<>(ins);
	}

	public <P1> DoBuilder<T> with(Consumer1<T, P1> consumer, P1 p1) {
		Consumer<T> c = instance -> consumer.accept(instance, p1);
		modifiers.add(c);
		return this;
	}

	public <P1, P2> DoBuilder<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
		modifiers.add(c);
		return this;
	}

	public <P1, P2, P3> DoBuilder<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
		modifiers.add(c);
		return this;
	}

	public T build() {
		T value = instance.get();
		modifiers.forEach(modifier -> modifier.accept(value));
		modifiers.clear();
		return value;
	}

	/**
	 * 1 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer1<T, P1> {
		void accept(T t, P1 p1);
	}

	/**
	 * 2 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer2<T, P1, P2> {
		void accept(T t, P1 p1, P2 p2);
	}

	/**
	 * 3 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer3<T, P1, P2, P3> {
		void accept(T t, P1 p1, P2 p2, P3 p3);
	}
}
