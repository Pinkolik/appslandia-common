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

package com.appslandia.common.base;

import java.util.Map;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ConfigWrapper implements Config {

	final Config cfg;

	public ConfigWrapper(Config cfg) {
		this.cfg = cfg;
	}

	// com.appslandia.common.base.Config

	@Override
	public String getString(String key) {
		return this.cfg.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return this.cfg.getString(key, defaultValue);
	}

	@Override
	public String getRequiredString(String key) throws IllegalStateException {
		return this.cfg.getRequiredString(key);
	}

	@Override
	public String[] getStringArray(String key) {
		return this.cfg.getStringArray(key);
	}

	@Override
	public String getFormatted(String key) {
		return this.cfg.getFormatted(key);
	}

	@Override
	public String getRequiredFormatted(String key) throws IllegalStateException {
		return this.cfg.getRequiredFormatted(key);
	}

	@Override
	public String getFormatted(String key, Map<String, Object> parameters) {
		return this.cfg.getFormatted(key, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Map<String, Object> parameters) throws IllegalStateException {
		return this.cfg.getRequiredFormatted(key, parameters);
	}

	@Override
	public String getFormatted(String key, Object... parameters) throws IllegalStateException {
		return this.cfg.getFormatted(key, parameters);
	}

	@Override
	public String getRequiredFormatted(String key, Object... parameters) throws IllegalStateException {
		return this.cfg.getRequiredFormatted(key, parameters);
	}

	@Override
	public boolean getBool(String key, boolean defaultValue) {
		return this.cfg.getBool(key, defaultValue);
	}

	@Override
	public boolean getRequiredBool(String key) throws IllegalStateException, BoolFormatException {
		return this.cfg.getRequiredBool(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return this.cfg.getInt(key, defaultValue);
	}

	@Override
	public int getRequiredInt(String key) throws IllegalStateException, NumberFormatException {
		return this.cfg.getRequiredInt(key);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return this.cfg.getLong(key, defaultValue);
	}

	@Override
	public long getRequiredLong(String key) throws IllegalStateException, NumberFormatException {
		return this.cfg.getRequiredLong(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return this.cfg.getDouble(key, defaultValue);
	}

	@Override
	public double getRequiredDouble(String key) throws IllegalStateException, NumberFormatException {
		return this.cfg.getRequiredDouble(key);
	}
}
