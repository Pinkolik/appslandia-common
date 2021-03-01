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

package com.appslandia.common.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.appslandia.common.base.Out;
import com.appslandia.common.jdbc.Sql;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ReflectionException;
import com.appslandia.common.utils.ReflectionUtils;
import com.appslandia.common.utils.ReflectionUtils.FieldHandler;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JpaUtils {

	public static String generateInsertSql(Class<?> entityClass) {
		Entity entity = entityClass.getDeclaredAnnotation(Entity.class);
		AssertUtils.assertNotNull(entity, "entityClass is not entity class.");

		Table table = entityClass.getDeclaredAnnotation(Table.class);

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		if ((table == null) || table.name().isEmpty()) {
			builder.append(!entity.name().isEmpty() ? entity.name() : entityClass.getSimpleName());
		} else {
			// table.name
			if (!table.catalog().isEmpty()) {
				AssertUtils.assertTrue(!table.schema().isEmpty());
				builder.append(table.catalog()).append('.').append(table.schema()).append('.').append(table.name());

			} else if (!table.schema().isEmpty()) {
				builder.append(table.schema()).append('.').append(table.name());
			} else {
				builder.append(table.name());
			}
		}
		builder.append(" (");

		final Out<Boolean> firstCol = new Out<Boolean>(true);
		ReflectionUtils.traverse(entityClass, new FieldHandler() {

			@Override
			public boolean matches(Field field) {
				return isInsertField(field);
			}

			@Override
			public boolean handle(Field field) throws ReflectionException {
				if (firstCol.val()) {
					builder.append(getColumnName(field));
					firstCol.value = false;
				} else {
					builder.append(',').append(getColumnName(field));
				}
				return true;
			}
		});
		builder.append(") VALUES (");

		// :columnNames
		firstCol.value = true;
		ReflectionUtils.traverse(entityClass, new FieldHandler() {

			@Override
			public boolean matches(Field field) {
				return isInsertField(field);
			}

			@Override
			public boolean handle(Field field) throws ReflectionException {
				if (firstCol.val()) {
					builder.append(Sql.getParamPrefix()).append(getColumnName(field));
					firstCol.value = false;
				} else {
					builder.append(',').append(Sql.getParamPrefix()).append(getColumnName(field));
				}
				return true;
			}
		});
		builder.append(")");
		return builder.toString();
	}

	static String getColumnName(Field field) {
		Column column = field.getDeclaredAnnotation(Column.class);
		if ((column == null) || column.name().isEmpty()) {
			return field.getName();
		}
		return column.name();
	}

	static boolean isInsertField(Field field) {
		if (field.getName().startsWith("_persistence_")) {
			return false;
		}
		if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
			return false;
		}
		if (field.getDeclaredAnnotation(Transient.class) != null) {
			return false;
		}
		if ((field.getDeclaredAnnotation(OneToOne.class) != null) || (field.getDeclaredAnnotation(OneToMany.class) != null)
				|| (field.getDeclaredAnnotation(ManyToOne.class) != null)) {
			return false;
		}
		if ((field.getDeclaredAnnotation(JoinTable.class) != null) || (field.getDeclaredAnnotation(JoinColumn.class) != null)
				|| (field.getDeclaredAnnotation(JoinColumns.class) != null)) {
			return false;
		}
		GeneratedValue gv = field.getDeclaredAnnotation(GeneratedValue.class);
		if (gv == null) {
			return true;
		}
		if (gv.strategy() == GenerationType.IDENTITY || gv.strategy() == GenerationType.SEQUENCE) {
			return false;
		}
		return true;
	}
}
