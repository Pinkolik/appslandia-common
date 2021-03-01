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

package com.appslandia.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.appslandia.common.base.DateFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DateUtils {

	// ISO8601
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	public static final String PATTERN_TIME = "HH:mm:ss.SSS";
	public static final String PATTERN_TIME_Z = "HH:mm:ss.SSSXXX";

	public static final String PATTERN_TIME_M = "HH:mm";
	public static final String PATTERN_TIME_MZ = "HH:mmXXX";

	public static final String PATTERN_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final String PATTERN_DATETIME_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static final String PATTERN_DATETIME_M = "yyyy-MM-dd'T'HH:mm";
	public static final String PATTERN_DATETIME_MZ = "yyyy-MM-dd'T'HH:mmXXX";

	public static final String PATTERN_YEAR_MONTH = "yyyy-MM";

	public static java.sql.Date todaySqlDate() {
		return new java.sql.Date(todayAsLong());
	}

	public static long todayAsLong() {
		return todayCalendar().getTimeInMillis();
	}

	public static Calendar todayCalendar() {
		Calendar cal = new GregorianCalendar();
		clearTime(cal);
		return cal;
	}

	public static java.sql.Timestamp nowTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	public static long clearMs(long timeInMs) {
		return (timeInMs / 1000) * 1000;
	}

	public static Date clearMs(Date dt) {
		return new Date(clearMs(dt.getTime()));
	}

	public static long clearTime(long timeInMs) {
		return clearTime(new Date(timeInMs)).getTime();
	}

	public static Date clearTime(Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		clearTime(cal);
		return cal.getTime();
	}

	public static void clearTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	public static Calendar getCalendar(int dayOfWeek, int atHour, int atMinute) {
		Calendar cal = new GregorianCalendar();

		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		cal.set(Calendar.HOUR_OF_DAY, atHour);
		cal.set(Calendar.MINUTE, atMinute);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Date copyTime(Date dest, Date src) {
		Calendar destCal = new GregorianCalendar();
		destCal.setTime(dest);

		Calendar srcCal = new GregorianCalendar();
		srcCal.setTime(src);

		destCal.set(Calendar.HOUR_OF_DAY, srcCal.get(Calendar.HOUR_OF_DAY));
		destCal.set(Calendar.MINUTE, srcCal.get(Calendar.MINUTE));
		destCal.set(Calendar.SECOND, srcCal.get(Calendar.SECOND));
		destCal.set(Calendar.MILLISECOND, srcCal.get(Calendar.MILLISECOND));
		return destCal.getTime();
	}

	public static boolean isFutureTime(long timeMillis, int leewayMs) {
		return System.currentTimeMillis() - leewayMs < timeMillis;
	}

	public static boolean isPastTime(long timeMillis, int leewayMs) {
		return System.currentTimeMillis() + leewayMs >= timeMillis;
	}

	public static String format(Date dt, String pattern) {
		return newDateFormat(pattern).format(dt);
	}

	public static SimpleDateFormat newDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf;
	}

	public static java.sql.Date iso8601Date(String date) throws DateFormatException {
		return (date != null) ? new java.sql.Date(parse(date, PATTERN_DATE).getTime()) : null;
	}

	public static String iso8601Date(Date date) {
		return (date != null) ? newDateFormat(PATTERN_DATE).format(date) : null;
	}

	public static java.sql.Time iso8601Time(String time) throws DateFormatException {
		return (time != null) ? new java.sql.Time(parse(time, PATTERN_TIME).getTime()) : null;
	}

	public static String iso8601Time(Date time) {
		return (time != null) ? newDateFormat(PATTERN_TIME).format(time) : null;
	}

	public static java.sql.Timestamp iso8601DateTime(String dateTime) throws DateFormatException {
		return (dateTime != null) ? new java.sql.Timestamp(parse(dateTime, PATTERN_DATETIME).getTime()) : null;
	}

	public static String iso8601DateTime(Date dateTime) {
		return (dateTime != null) ? newDateFormat(PATTERN_DATETIME).format(dateTime) : null;
	}

	public static Date parse(String dt, String pattern) throws DateFormatException {
		try {
			return newDateFormat(pattern).parse(dt);
		} catch (ParseException ex) {
			throw new DateFormatException(ex);
		}
	}

	// Java8 Date/Time

	private static final class DateTimeFormattersHolder {
		private static final ConcurrentMap<String, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();
	}

	public static DateTimeFormatter getFormatter(String pattern) {
		return DateTimeFormattersHolder.FORMATTERS.computeIfAbsent(pattern, p -> DateTimeFormatter.ofPattern(p));
	}

	public static OffsetDateTime nowAtUTC() {
		return nowAt(ZoneOffset.UTC);
	}

	public static OffsetDateTime nowAt(String offsetId) {
		return nowAt(ZoneOffset.of(offsetId));
	}

	public static OffsetDateTime nowAt(ZoneOffset offset) {
		return OffsetDateTime.now().withOffsetSameInstant(offset);
	}

	public static OffsetDateTime atUTC(OffsetDateTime odt) {
		return odt.withOffsetSameInstant(ZoneOffset.UTC);
	}

	public static LocalDate firstDayOfMonth(LocalDate ld) {
		return ld.with(TemporalAdjusters.firstDayOfMonth());
	}

	public static LocalDate lastDayOfMonth(LocalDate ld) {
		return ld.with(TemporalAdjusters.lastDayOfMonth());
	}

	public static LocalDateTime firstDayOfMonth(LocalDateTime ldt) {
		return ldt.with(TemporalAdjusters.firstDayOfMonth());
	}

	public static LocalDateTime lastDayOfMonth(LocalDateTime ldt) {
		return ldt.with(TemporalAdjusters.lastDayOfMonth());
	}

	public static OffsetDateTime firstDayOfMonth(OffsetDateTime odt) {
		return odt.with(TemporalAdjusters.firstDayOfMonth());
	}

	public static OffsetDateTime lastDayOfMonth(OffsetDateTime odt) {
		return odt.with(TemporalAdjusters.lastDayOfMonth());
	}

	public static LocalDate iso8601LocalDate(String date) throws DateTimeParseException {
		return (date != null) ? LocalDate.parse(date, getFormatter(PATTERN_DATE)) : null;
	}

	public static String iso8601LocalDate(LocalDate date) {
		return (date != null) ? getFormatter(PATTERN_DATE).format(date) : null;
	}

	public static LocalTime iso8601LocalTime(String time) throws DateTimeParseException {
		return (time != null) ? LocalTime.parse(time, getFormatter(PATTERN_TIME)) : null;
	}

	public static String iso8601LocalTime(LocalTime time) {
		return (time != null) ? getFormatter(PATTERN_TIME).format(time) : null;
	}

	public static LocalDateTime iso8601LocalDateTime(String dateTime) throws DateTimeParseException {
		return (dateTime != null) ? LocalDateTime.parse(dateTime, getFormatter(PATTERN_DATETIME)) : null;
	}

	public static String iso8601LocalDateTime(LocalDateTime dateTime) {
		return (dateTime != null) ? getFormatter(PATTERN_DATETIME).format(dateTime) : null;
	}

	public static OffsetTime iso8601OffsetTime(String timeZ) throws DateTimeParseException {
		return (timeZ != null) ? OffsetTime.parse(timeZ, getFormatter(PATTERN_TIME_Z)) : null;
	}

	public static String iso8601OffsetTime(OffsetTime timeZ) {
		return (timeZ != null) ? getFormatter(PATTERN_TIME_Z).format(timeZ) : null;
	}

	public static OffsetDateTime iso8601OffsetDateTime(String dateTimeZ) throws DateTimeParseException {
		return (dateTimeZ != null) ? OffsetDateTime.parse(dateTimeZ, getFormatter(PATTERN_DATETIME_Z)) : null;
	}

	public static String iso8601OffsetDateTime(OffsetDateTime dateTimeZ) {
		return (dateTimeZ != null) ? getFormatter(PATTERN_DATETIME_Z).format(dateTimeZ) : null;
	}

	// 1w 2d 3h 4m 50s 500ms
	private static final Pattern TEMPORAL_AMT_PATTERN = Pattern.compile("((\\d+.\\d+|\\d+)(w|d|h|m|s|ms)\\s*)+", Pattern.CASE_INSENSITIVE);

	public static long translateToMs(String temporalAmt) throws IllegalArgumentException {
		temporalAmt = StringUtils.trimToNull(temporalAmt);
		AssertUtils.assertNotNull(temporalAmt, "temporalAmt is required.");

		if (!TEMPORAL_AMT_PATTERN.matcher(temporalAmt).matches()) {
			throw new IllegalArgumentException("temporalAmt is invalid (value=" + temporalAmt + ")");
		}

		double result = 0l;
		int i = 0;

		while (i < temporalAmt.length()) {
			int j = i;
			while (Character.isDigit(temporalAmt.charAt(j)) || (temporalAmt.charAt(j) == '.'))
				j++;
			int k = j;
			while ((k <= temporalAmt.length() - 1) && (Character.isLetter(temporalAmt.charAt(k)) || (temporalAmt.charAt(k) == (' '))))
				k++;

			double amt = Double.parseDouble(temporalAmt.substring(i, j));
			String unit = temporalAmt.substring(j, k).trim().toLowerCase(Locale.ENGLISH);

			switch (unit) {
			case "w":
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * 7 * amt;
				break;
			case "d":
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * amt;
				break;
			case "h":
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS) * amt;
				break;
			case "m":
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) * amt;
				break;
			case "s":
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS) * amt;
				break;
			default:
				result += amt;
				break;
			}
			i = k;
		}
		return (long) Math.ceil(result);
	}

	public static Map<TimeUnit, Long> parseUnits(long duration, TimeUnit unit) {
		return parseUnits(duration, unit, TimeUnit.DAYS, TimeUnit.NANOSECONDS);
	}

	public static Map<TimeUnit, Long> parseUnits(long duration, TimeUnit unit, TimeUnit highUnit, TimeUnit lowUnit) {
		AssertUtils.assertTrue(highUnit.compareTo(lowUnit) >= 0, "highUnit must be gte lowUnit.");
		Map<TimeUnit, Long> res = new EnumMap<>(TimeUnit.class);

		duration = lowUnit.convert(duration, unit);
		TimeUnit u = highUnit;

		while (true) {
			long v = u.convert(duration, lowUnit);
			res.put(u, v);

			if (u == lowUnit) {
				break;
			}
			duration -= lowUnit.convert(v, u);
			u = nextLowerUnit(u);
		}
		return res;
	}

	public static TimeUnit nextLowerUnit(TimeUnit unit) {
		switch (unit) {
		case DAYS:
			return TimeUnit.HOURS;
		case HOURS:
			return TimeUnit.MINUTES;
		case MINUTES:
			return TimeUnit.SECONDS;
		case SECONDS:
			return TimeUnit.MILLISECONDS;
		case MILLISECONDS:
			return TimeUnit.MICROSECONDS;
		case MICROSECONDS:
			return TimeUnit.NANOSECONDS;
		default:
			throw new Error();
		}
	}
}
