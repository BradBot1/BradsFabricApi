package com.bb1.fabric.bfapi.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Inputs {
	
	public static Input<?> createAppropriateInputFor(@NotNull Object input, @Nullable Object... inputs) {
		return switch ((input==null) ? 1 : inputs.length+1 ){
		case 1 -> new Input<>(input);
		case 2 -> new DualInput<>(input, inputs[0]);
		case 3 -> new TriInput<>(input, inputs[0], inputs[1]);
		case 4 -> new QuadInput<>(input, inputs[0], inputs[1], inputs[2]); 
		default -> new DynamicInput(input, inputs); // We don't have an input that can hold the amount needed, so we default to returning an input with the raw inputed arguments
		};
	}
	
	public static class Input<I> {
		
		public static <I> Input<I> of(I i) { return new Input<I>(i); }
		
		protected final I i;
		
		public Input(I i) {
			this.i = i;
		}
		
		public I get() { return this.i; }
		
		public Field<?>[] getAll() { return new Field<?>[] { Field.of(this.i) }; }
		
		public Object[] getAllRaw() { return new Object[] { this.i }; }
		
	}
	
	public static class DualInput<I, I2> extends Input<I> {
		
		public static <I, I2> DualInput<I, I2> of(I i, I2 i2) { return new DualInput<I, I2>(i, i2); }
		
		protected final I2 i2;
		
		public DualInput(I i, I2 i2) {
			super(i);
			this.i2 = i2;
		}
		
		public I2 getSecond() { return this.i2; }
		
		@Override
		public Field<?>[] getAll() { return new Field<?>[] { Field.of(this.i), Field.of(this.i2) }; }
		
		@Override
		public Object[] getAllRaw() { return new Object[] { this.i, this.i2 }; }
		
	}
	
	public static class TriInput<I, I2, I3> extends DualInput<I, I2> {
		
		public static <I, I2, I3> TriInput<I, I2, I3> of(I i, I2 i2, I3 i3) { return new TriInput<I, I2, I3>(i, i2, i3); }
		
		protected final I3 i3;
		
		public TriInput(I i, I2 i2, I3 i3) {
			super(i, i2);
			this.i3 = i3;
		}
		
		public I3 getThird() { return this.i3; }
		
		@Override
		public Field<?>[] getAll() { return new Field<?>[] { Field.of(this.i), Field.of(this.i2), Field.of(this.i3) }; }
		
		@Override
		public Object[] getAllRaw() { return new Object[] { this.i, this.i2, this.i3 }; }
		
	}
	
	public static class QuadInput<I, I2, I3, I4> extends TriInput<I, I2, I3> {
		
		public static <I, I2, I3, I4> QuadInput<I, I2, I3, I4> of(I i, I2 i2, I3 i3, I4 i4) { return new QuadInput<I, I2, I3, I4>(i, i2, i3, i4); }
		
		protected final I4 i4;
		
		public QuadInput(I i, I2 i2, I3 i3, I4 i4) {
			super(i, i2, i3);
			this.i4 = i4;
		}
		
		public I4 getForth() { return this.i4; }
		
		@Override
		public Field<?>[] getAll() { return new Field<?>[] { Field.of(this.i), Field.of(this.i2), Field.of(this.i3), Field.of(this.i4) }; }
		
		@Override
		public Object[] getAllRaw() { return new Object[] { this.i, this.i2, this.i3, this.i4 }; }
		
	}
	
	public static class QuintInput<I, I2, I3, I4, I5> extends QuadInput<I, I2, I3, I4> {
		
		public static <I, I2, I3, I4, I5> QuintInput<I, I2, I3, I4, I5> of(I i, I2 i2, I3 i3, I4 i4, I5 i5) { return new QuintInput<I, I2, I3, I4, I5>(i, i2, i3, i4, i5); }
		
		protected final I5 i5;
		
		public QuintInput(I i, I2 i2, I3 i3, I4 i4, I5 i5) {
			super(i, i2, i3, i4);
			this.i5 = i5;
		}
		
		public I5 getFith() { return this.i5; }
		
		@Override
		public Field<?>[] getAll() { return new Field<?>[] { Field.of(this.i), Field.of(this.i2), Field.of(this.i3), Field.of(this.i4), Field.of(this.i5) }; }
		
		@Override
		public Object[] getAllRaw() { return new Object[] { this.i, this.i2, this.i3, this.i4, this.i5 }; }
		
	}
	
	public static class DynamicInput extends Input<Void> {
		
		public static DynamicInput of(Object value, Object... others) {
			return null;
		}
		
		private List<Object> _inputs = new ArrayList<Object>();
		
		public DynamicInput(Object value, Object... others) {
			super(null);
			this._inputs.add(value);
			if (others!=null) {
				for (Object object : others) {
					this._inputs.add(object);
				}
			}
		}
		
		public void add(Object newInput) {
			this._inputs.add(newInput);
		}
		
		@Override
		public Field<?>[] getAll() {
			Field<?>[] fields = new Field<?>[this._inputs.size()];
			int index = 0;
			for (Object input : this._inputs) {
				fields[index++] = Field.of(input);
			}
			return fields;
		}
		
		@Override
		public Object[] getAllRaw() { return this._inputs.toArray(); }
		
	}
	
}
