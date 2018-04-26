package com.travlog.android.apps.libs.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    /**
     * e.g.: December 2015.
     */
    public static @NonNull
    String estimatedDeliveryOn(final @NonNull DateTime dateTime) {
        return estimatedDeliveryOn(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: December 2015.
     */
    public static @NonNull
    String estimatedDeliveryOn(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.forPattern(localePattern(locale)).withLocale(locale).withZoneUTC());
    }

    public static boolean isDateToday(final @NonNull DateTime dateTime) {
        return dateTime.withZone(DateTimeZone.UTC).withTimeAtStartOfDay()
                .equals(DateTime.now().withTimeAtStartOfDay().withZoneRetainFields(DateTimeZone.UTC));
    }

    /**
     * Returns a boolean indicating whether or not a DateTime value is the Epoch. Returns `true` if the
     * DateTime equals 1970-01-01T00:00:00Z.
     */
    public static boolean isEpoch(final @NonNull DateTime dateTime) {
        return dateTime.getMillis() == 0;
    }

    /**
     * e.g.: Tuesday, June 20, 2017
     */
    public static @NonNull
    String fullDate(final @NonNull DateTime dateTime) {
        return fullDate(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: Tuesday, June 20, 2017
     */
    public static @NonNull
    String fullDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        try {
            return dateTime.toString(DateTimeFormat.fullDate().withLocale(locale).withZoneUTC());
        } catch (final IllegalArgumentException e) {
            // JodaTime doesn't support the 'cccc' pattern, triggered by fullDate and fullDateTime. See: https://github.com/dlew/joda-time-android/issues/30
            // Instead just return a medium date.
            return mediumDate(dateTime, locale);
        }
    }

    public static @NonNull
    String relativeFullDate(final @NonNull DateTime dateTime) {
        return relativeFullDate(dateTime, Locale.getDefault());
    }

    public static @NonNull
    String relativeFullDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        final DateTime current = new DateTime();
        final String mediumShortStyle = String.format("%s E", DateTimeFormat.patternForStyle("M-", locale));
        final DateTimeFormatter formatter;

        if (dateTime.getYear() == current.getYear()) {
            formatter = DateTimeFormat.forPattern(mediumShortStyle);
        } else {
            formatter = DateTimeFormat.forPattern(mediumShortStyle);
        }
        return dateTime.toString(formatter.withZone(DateTimeZone.getDefault()).withLocale(locale));
    }

    /**
     * Returns the proper DateTime format pattern for supported locales.
     */
    private static @NonNull
    String localePattern(final @NonNull Locale locale) {
        switch (locale.getLanguage()) {
            case "de":
                return "MMMM yyyy";
            case "en":
                return "MMMM yyyy";
            case "es":
                return "MMMM yyyy";
            case "fr":
                return "MMMM yyyy";
            case "ja":
                return "yyyy'年'MMMM"; // NB Japanese in general should show year before month
            default:
                return "MMMM yyyy";
        }
    }

    /**
     * e.g.: June 20, 2017
     */
    public static @NonNull
    String longDate(final @NonNull DateTime dateTime) {
        return longDate(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: June 20, 2017
     */
    public static @NonNull
    String longDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.longDate().withLocale(locale).withZoneUTC());
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    public static @NonNull
    String mediumDate(final @NonNull DateTime dateTime) {
        return mediumDate(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    public static @NonNull
    String mediumDate(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.mediumDate().withLocale(locale).withZoneUTC());
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    public static @NonNull
    String mediumDateShortTime(final @NonNull DateTime dateTime) {
        return mediumDateShortTime(dateTime, DateTimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    public static @NonNull
    String mediumDateShortTime(final @NonNull DateTime dateTime, final @NonNull DateTimeZone dateTimeZone) {
        return mediumDateShortTime(dateTime, dateTimeZone, Locale.getDefault());
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    public static @NonNull
    String mediumDateShortTime(final @NonNull DateTime dateTime, final @NonNull DateTimeZone dateTimeZone,
                               final @NonNull Locale locale) {
        final String mediumShortStyle = DateTimeFormat.patternForStyle("MS", locale);
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(mediumShortStyle).withZone(dateTimeZone).withLocale(locale);
        return dateTime.toString(formatter);
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    public static @NonNull
    String mediumDateTime(final @NonNull DateTime dateTime) {
        return mediumDateTime(dateTime, DateTimeZone.getDefault());
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    public static @NonNull
    String mediumDateTime(final @NonNull DateTime dateTime, final @NonNull DateTimeZone dateTimeZone) {
        return mediumDateTime(dateTime, dateTimeZone, Locale.getDefault());
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    public static @NonNull
    String mediumDateTime(final @NonNull DateTime dateTime, final @NonNull DateTimeZone dateTimeZone,
                          final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.mediumDateTime().withLocale(locale).withZone(dateTimeZone));
    }

    public static @NonNull
    String relative(final @NonNull DateTime start, final @NonNull DateTime end) {
        final Years years = Years.yearsBetween(start, end);
        final int yearsDifference = years.getYears();

        String startStr;
        String endStr;
        if (yearsDifference != 0) {
            startStr = start.toString("dd MMMM yyyy");
            endStr = end.toString("dd MMMM yyyy");
        } else {
            final Months months = Months.monthsBetween(start, end);
            final int monthDifference = months.getMonths();

            if (monthDifference != 0) {
                startStr = start.toString("dd MMMM");
                endStr = end.toString("dd MMMM");
            } else {
                startStr = start.toString("dd");
                endStr = end.toString("dd MMMM");
            }
        }
        return String.format("%s - %s", startStr, endStr);
    }

    /**
     * e.g.: 4:20 PM
     */
    public static @NonNull
    String shortTime(final @NonNull DateTime dateTime) {
        return shortTime(dateTime, Locale.getDefault());
    }

    /**
     * e.g.: 4:20 PM
     */
    public static @NonNull
    String shortTime(final @NonNull DateTime dateTime, final @NonNull Locale locale) {
        return dateTime.toString(DateTimeFormat.shortTime().withLocale(locale).withZoneUTC());
    }

    /**
     * Utility to pair a unit (e.g. "minutes", "hours", "days") with a measurement. Returns `null` if the difference
     * exceeds the threshold.
     */
    private static @Nullable
    Pair<String, Integer> unitAndDifference(final int initialSecondsDifference, final int threshold) {
        final int secondsDifference = Math.abs(initialSecondsDifference);
        final int daysDifference = (int) Math.floor(secondsDifference / 86400);

        if (secondsDifference < 3600) { // 1 hour
            final int minutesDifference = (int) Math.floor(secondsDifference / 60.0);
            return new Pair<>("minutes", minutesDifference);
        } else if (secondsDifference < 86400) { // 24 hours
            final int hoursDifference = (int) Math.floor(secondsDifference / 60.0 / 60.0);
            return new Pair<>("hours", hoursDifference);
        } else if (secondsDifference < threshold) {
            return new Pair<>("days", daysDifference);
        }

        return null;
    }
}
