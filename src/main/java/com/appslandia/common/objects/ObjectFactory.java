// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ObjectUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ObjectFactory extends InitializeObject {

	final List<ObjectInstance> instances = new ArrayList<>();

	@Override
	protected void init() throws Exception {
		validateFactory();
	}

	private void validateFactory() throws ObjectException {
		for (ObjectInstance inst : this.instances) {
			if (inst.definition.getProducer() != null) {
				continue;
			}
			new InjectTraverser() {

				@Override
				public boolean isValidationContext() {
					return true;
				}

				@Override
				public void onParameter(Parameter parameter) throws ObjectException {
					validateInject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter), parameter);
				}

				@Override
				public void onField(Field field) throws ObjectException {
					validateInject(field.getType(), AnnotationUtils.parseQualifiers(field), field);
				}

				@Override
				public void onMethod(Method method) throws ObjectException {
					throw new UnsupportedOperationException();
				}

				private String toMemberInfo(AnnotatedElement member) {
					if (member instanceof Parameter) {
						return ((Parameter) member).getDeclaringExecutable().toString();
					}
					return member.toString();
				}

				private void validateInject(Class<?> type, Annotation[] qualifiers, AnnotatedElement member) throws ObjectException {
					if (ObjectFactory.class.isAssignableFrom(type)) {
						return;
					}
					if (type == Instance.class) {
						return;
					}
					int count = countMatchesForInject(type, qualifiers);
					if (count == 0) {
						throw new ObjectException("Unsatisfied dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers) + ", member="
								+ toMemberInfo(member) + ")");
					}
					if (count > 1) {
						throw new ObjectException("Ambiguous dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers) + ", member="
								+ toMemberInfo(member) + ")");
					}
				}

			}.traverse(inst.definition.getImplClass());
		}
	}

	private int countMatchesForInject(Class<?> type, Annotation[] qualifiers) {
		int count = 0;
		for (ObjectInstance inst : this.instances) {
			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				count++;
			}
		}
		return count;
	}

	public ObjectFactory register(Class<?> type, ObjectProducer<?> producer) {
		return register(type, producer, null);
	}

	public ObjectFactory register(Class<?> type, ObjectProducer<?> producer, ObjectScope scope) {
		return register(type, producer, scope, null);
	}

	public ObjectFactory register(Class<?> type, ObjectProducer<?> producer, ObjectScope scope, Annotation[] qualifiers) {
		return register(new Class<?>[] { type }, producer, scope, qualifiers);
	}

	public ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer) {
		return register(types, producer, null);
	}

	public ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer, ObjectScope scope) {
		return register(types, producer, scope, null);
	}

	protected ObjectFactory register(Class<?>[] types, ObjectProducer<?> producer, ObjectScope scope, Annotation[] qualifiers) {
		assertNotInitialized();

		AssertUtils.assertNotNull(types);
		AssertUtils.assertNotNull(producer);

		if (scope == null) {
			scope = AnnotationUtils.parseScope(producer);
		}

		if (qualifiers == null) {
			qualifiers = AnnotationUtils.parseQualifiers(producer);
		}

		ObjectInstance inst = new ObjectInstance(new ObjectDefinition().setTypes(types).setQualifiers(qualifiers).setScope(scope).setProducer(producer),
				(d) -> produceObject(d));
		this.instances.add(inst);
		return this;
	}

	public ObjectFactory register(Class<?> type, Class<?> implClass) {
		return register(type, implClass, null);
	}

	public ObjectFactory register(Class<?> type, Class<?> implClass, ObjectScope scope) {
		return register(type, implClass, scope, null);
	}

	public ObjectFactory register(Class<?> type, Class<?> implClass, ObjectScope scope, Annotation[] qualifiers) {
		return register(new Class<?>[] { type }, implClass, scope, qualifiers);
	}

	public ObjectFactory register(Class<?>[] types, Class<?> implClass) {
		return register(types, implClass, null);
	}

	public ObjectFactory register(Class<?>[] types, Class<?> implClass, ObjectScope scope) {
		return register(types, implClass, scope, null);
	}

	protected ObjectFactory register(Class<?>[] types, Class<?> implClass, ObjectScope scope, Annotation[] qualifiers) {
		assertNotInitialized();
		AssertUtils.assertNotNull(types);
		AssertUtils.assertNotNull(implClass);

		Set<Class<?>> expTypes = CollectionUtils.toSet(types);
		expTypes.add(implClass);

		if (scope == null) {
			scope = AnnotationUtils.parseScope(implClass);
		}

		if (qualifiers == null) {
			qualifiers = AnnotationUtils.parseQualifiers(implClass);
		}

		ObjectInstance inst = new ObjectInstance(new ObjectDefinition().setTypes(expTypes.toArray(new Class<?>[expTypes.size()])).setQualifiers(qualifiers)
				.setScope(scope).setImplClass(implClass), (d) -> produceObject(d));
		this.instances.add(inst);
		return this;
	}

	public ObjectFactory unregister(Class<?> type) {
		return unregister(type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public ObjectFactory unregister(Class<?> type, Annotation... qualifiers) {
		assertNotInitialized();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(qualifiers);

		Iterator<ObjectInstance> iter = this.instances.iterator();
		while (iter.hasNext()) {
			ObjectInstance inst = iter.next();

			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				iter.remove();
			}
		}
		return this;
	}

	public <T> ObjectFactory unregister(Class<T> type, Class<? extends T> implClass) {
		AssertUtils.assertNotNull(implClass);
		return unregister(type, AnnotationUtils.parseQualifiers(implClass));
	}

	public <T> Instance<T> select(Class<T> subtype, Annotation... qualifiers) throws ObjectException {
		List<ObjectInstance> insts = getObjectInsts(subtype, qualifiers);
		return new InstanceImpl<>(subtype, qualifiers, insts);
	}

	public ObjectFactory inject(final Object obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);

		new InjectTraverser() {

			@Override
			public boolean isValidationContext() {
				return false;
			}

			@Override
			public void onParameter(Parameter parameter) throws ObjectException {
				throw new UnsupportedOperationException();
			}

			@Override
			public void onField(Field field) throws ObjectException {
				try {
					field.setAccessible(true);
					Annotation[] qualifiers = AnnotationUtils.parseQualifiers(field);

					// @Inject T t;
					if (field.getType() != Instance.class) {
						Object value = getObject(field.getType(), qualifiers);
						field.set(obj, value);
						return;
					}

					// @Inject @Instance<T>
					AssertUtils.assertTrue(field.getGenericType() instanceof ParameterizedType);
					Type argType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					AssertUtils.assertTrue(argType instanceof Class);
					Class<?> type = (Class<?>) argType;

					// InstanceImpl
					List<ObjectInstance> insts = getObjectInsts(type, qualifiers);
					field.set(obj, new InstanceImpl<>(type, qualifiers, insts));

				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
			}

			@Override
			public void onMethod(Method method) throws ObjectException {
				try {
					method.setAccessible(true);
					method.invoke(obj, createArguments(method.getParameters()));

				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
			}
		}.traverse(obj.getClass());
		return this;
	}

	private Object[] createArguments(Parameter[] parameters) throws ObjectException {
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			args[i] = getObject(parameter.getType(), AnnotationUtils.parseQualifiers(parameter));
		}
		return args;
	}

	private Object produceObject(ObjectDefinition definition) throws ObjectException {
		try {
			// Producer
			if (definition.getProducer() != null) {
				return definition.getProducer().produce(this);
			}
			// Constructor
			Constructor<?> emptyCtor = null, injectCtor = null;
			for (Constructor<?> ctor : definition.getImplClass().getDeclaredConstructors()) {
				if (ctor.getDeclaredAnnotation(Inject.class) != null) {
					injectCtor = ctor;
					break;
				}
				if (ctor.getParameterCount() == 0) {
					emptyCtor = ctor;
				}
			}
			if ((injectCtor == null) && (emptyCtor == null)) {
				throw new ObjectException("Could not instantiate (implClass=" + definition.getImplClass() + ")");
			}
			Object instance = null;
			if (injectCtor != null) {
				injectCtor.setAccessible(true);
				instance = injectCtor.newInstance(createArguments(injectCtor.getParameters()));
			} else {
				emptyCtor.setAccessible(true);
				instance = emptyCtor.newInstance(ReflectionUtils.EMPTY_OBJECTS);
			}
			return this.inject(instance).postConstruct(instance);

		} catch (ObjectException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ObjectException(ex);
		}
	}

	private List<ObjectInstance> getObjectInsts(Class<?> type, Annotation[] subQualifiers) throws ObjectException {
		List<ObjectInstance> insts = new ArrayList<>();
		for (ObjectInstance inst : this.instances) {

			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.hasAnnotations(inst.definition.getQualifiers(), subQualifiers)) {
				insts.add(inst);
			}
		}
		return insts;
	}

	private ObjectInstance getObjectInst(Class<?> type, Annotation[] qualifiers) throws ObjectException {
		ObjectInstance obj = null;
		for (ObjectInstance inst : this.instances) {
			if ((inst.definition.hasType(type) || (type == Object.class)) && AnnotationUtils.equals(inst.definition.getQualifiers(), qualifiers)) {
				if (obj != null) {
					throw new ObjectException("Ambiguous dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers));
				}
				obj = inst;
			}
		}
		return obj;
	}

	public <T, I extends T> I getObject(Class<T> type) throws ObjectException {
		return getObject(type, ReflectionUtils.EMPTY_ANNOTATIONS);
	}

	public <T, I extends T> I getObject(Class<T> type, Annotation... qualifiers) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(type);
		AssertUtils.assertNotNull(qualifiers);

		if (ObjectFactory.class.isAssignableFrom(type)) {
			return ObjectUtils.cast(this);
		}

		ObjectInstance inst = getObjectInst(type, qualifiers);
		if (inst == null) {
			throw new ObjectException("Unsatisfied dependency (type=" + type + ", qualifiers=" + Arrays.toString(qualifiers));
		}
		return ObjectUtils.cast(inst.getInstance());
	}

	public <T> T postConstruct(T obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);
		ReflectionUtils.traverse(obj.getClass(), new ReflectionUtils.MethodHandler() {

			@Override
			public boolean matches(Method m) {
				return m.getDeclaredAnnotation(PostConstruct.class) != null;
			}

			@Override
			public boolean handle(Method m) throws ReflectionException {
				try {
					m.setAccessible(true);
					m.invoke(obj);
				} catch (ObjectException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ObjectException(ex);
				}
				return false;
			}
		});
		return obj;
	}

	public <T> T preDestroy(T obj) throws ObjectException {
		initialize();
		AssertUtils.assertNotNull(obj);
		ObjectFactoryUtils.destroy(obj);
		return obj;
	}

	@Override
	public void destroy() throws ObjectException {
		for (ObjectInstance inst : this.instances) {
			Object obj = inst.getInstance();
			if (obj == null) {
				continue;
			}
			if (inst.definition.getProducer() == null) {
				preDestroy(obj);
			} else {
				inst.definition.getProducer().destroy(obj);
			}
			inst.clearInstance();
		}
	}

	public Iterator<ObjectDefinition> getDefinitionIterator() {
		return new Iterator<ObjectDefinition>() {
			int index = -1;

			@Override
			public ObjectDefinition next() {
				ObjectInstance inst = instances.get(++this.index);
				return inst.definition;
			}

			@Override
			public boolean hasNext() {
				return this.index < instances.size() - 1;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static abstract class InjectTraverser {

		public abstract boolean isValidationContext();

		public abstract void onParameter(Parameter parameter) throws ObjectException;

		public abstract void onField(Field field) throws ObjectException;

		public abstract void onMethod(Method method) throws ObjectException;

		public void traverse(Class<?> implClass) throws ObjectException {
			Class<?> clazz = null;
			if (isValidationContext()) {
				// Constructor
				for (Constructor<?> ctor : implClass.getDeclaredConstructors()) {
					if (ctor.getDeclaredAnnotation(Inject.class) != null) {
						for (Parameter parameter : ctor.getParameters()) {
							onParameter(parameter);
						}
						break;
					}
				}

				// Method
				clazz = implClass;
				while (clazz != Object.class) {
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getDeclaredAnnotation(Inject.class) != null) {
							for (Parameter parameter : method.getParameters()) {
								onParameter(parameter);
							}
						}
					}
					clazz = clazz.getSuperclass();
				}
			}

			// Fields
			clazz = implClass;
			while (clazz != Object.class) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (field.getDeclaredAnnotation(Inject.class) != null) {
						onField(field);
					}
				}
				clazz = clazz.getSuperclass();
			}

			// Injection Context
			if (!isValidationContext()) {
				clazz = implClass;
				while (clazz != Object.class) {
					Method[] methods = clazz.getDeclaredMethods();
					for (Method method : methods) {
						if (method.getDeclaredAnnotation(Inject.class) != null) {
							onMethod(method);
						}
					}
					clazz = clazz.getSuperclass();
				}
			}
		}
	}
}
