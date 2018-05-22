package com.travlog.android.apps.libs.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Months
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

object DateTimeUtils {

    /**
     * e.g.: December 2015.
     */
    fun estimatedDeliveryOn(dateTime: DateTime): String {
        return estimatedDeliveryOn(dateTime, Locale.getDefault())
    }

    /**
     * e.g.: December 2015.
     */
    fun estimatedDeliveryOn(dateTime: DateTime, locale: Locale): String {
        return dateTime.toString(DateTimeFormat.forPattern(localePattern(locale)).withLocale(locale).withZone(DateTimeZone.getDefault()))
    }

    fun isDateToday(dateTime: DateTime): Boolean {
        return dateTime.withZone(DateTimeZone.UTC).withTimeAtStartOfDay() == DateTime.now().withTimeAtStartOfDay().withZoneRetainFields(DateTimeZone.UTC)
    }

    /**
     * Returns a boolean indicating whether or not a DateTime value is the Epoch. Returns `true` if the
     * DateTime equals 1970-01-01T00:00:00Z.
     */
    fun isEpoch(dateTime: DateTime): Boolean {
        return dateTime.millis == 0L
    }

    /**
     * e.g.: Tuesday, June 20, 2017
     */
    fun fullDate(dateTime: DateTime): String {
        return fullDate(dateTime, Locale.getDefault())
    }

    /**
     * e.g.: Tuesday, June 20, 2017
     */
    fun fullDate(dateTime: DateTime, locale: Locale): String {
        return try {
            dateTime.toString(DateTimeFormat.fullDate().withLocale(locale).withZone(DateTimeZone.getDefault()))
        } catch (e: IllegalArgumentException) {
            // JodaTime doesn't support the 'cccc' pattern, triggered by fullDate and fullDateTime. See: https://github.com/dlew/joda-time-android/issues/30
            // Instead just return a medium date.
            mediumDate(dateTime, locale)
        }
    }

    fun relativeFullDate(dateTime: DateTime): String {
        return relativeFullDate(dateTime, Locale.getDefault())
    }

    fun relativeFullDate(dateTime: DateTime, locale: Locale): String {
        val current = DateTime()
        val mediumShortStyle = String.format("%s E", DateTimeFormat.patternForStyle("M-", locale))
        val formatter: DateTimeFormatter

        formatter = if (dateTime.year == current.year) {
            DateTimeFormat.forPattern(mediumShortStyle)
        } else {
            DateTimeFormat.forPattern(mediumShortStyle)
        }
        return dateTime.toString(formatter.withZone(DateTimeZone.getDefault()).withLocale(locale))
    }

    /**
     * Returns the proper DateTime format pattern for supported locales.
     */
    fun localePattern(locale: Locale): String {
        return when (locale.language) {
            "de" -> "MMMM yyyy"
            "en" -> "MMMM yyyy"
            "es" -> "MMMM yyyy"
            "fr" -> "MMMM yyyy"
            "ja" -> "yyyy'年'MMMM" // NB Japanese in general should show year before month
            else -> "MMMM yyyy"
        }
    }

    /**
     * e.g.: June 20, 2017
     */
    fun longDate(dateTime: DateTime): String {
        return longDate(dateTime, Locale.getDefault())
    }

    /**
     * e.g.: June 20, 2017
     */
    fun longDate(dateTime: DateTime, locale: Locale): String {
        return dateTime.toString(DateTimeFormat.longDate().withLocale(locale).withZone(DateTimeZone.getDefault()))
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    fun mediumDate(dateTime: DateTime): String {
        return mediumDate(dateTime, Locale.getDefault())
    }

    /**
     * e.g.: Dec 17, 2015.
     */
    fun mediumDate(dateTime: DateTime, locale: Locale): String {
        return dateTime.toString(DateTimeFormat.mediumDate().withLocale(locale).withZone(DateTimeZone.getDefault()))
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    fun mediumDateShortTime(dateTime: DateTime): String {
        return mediumDateShortTime(dateTime, DateTimeZone.getDefault(), Locale.getDefault())
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    fun mediumDateShortTime(dateTime: DateTime, dateTimeZone: DateTimeZone): String {
        return mediumDateShortTime(dateTime, dateTimeZone, Locale.getDefault())
    }

    /**
     * e.g.: Jan 14, 2016 2:20 PM.
     */
    fun mediumDateShortTime(dateTime: DateTime, dateTimeZone: DateTimeZone, locale: Locale): String {
        val mediumShortStyle = DateTimeFormat.patternForStyle("MS", locale)
        val formatter = DateTimeFormat.forPattern(mediumShortStyle).withZone(dateTimeZone).withLocale(locale)
        return dateTime.toString(formatter)
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    fun mediumDateTime(dateTime: DateTime): String {
        return mediumDateTime(dateTime, DateTimeZone.getDefault())
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    fun mediumDateTime(dateTime: DateTime, dateTimeZone: DateTimeZone): String {
        return mediumDateTime(dateTime, dateTimeZone, Locale.getDefault())
    }

    /**
     * e.g.: Dec 17, 2015 6:35:05 PM.
     */
    fun mediumDateTime(dateTime: DateTime, dateTimeZone: DateTimeZone, locale: Locale): String {
        return dateTime.toString(DateTimeFormat.mediumDateTime().withLocale(locale).withZone(dateTimeZone))
    }

    fun relative(start: DateTime, end: DateTime): String {
        val years = Years.yearsBetween(start, end)
        val yearsDifference = years.years

        val startStr: String
        val endStr: String
        if (yearsDifference != 0) {
            startStr = start.toString("dd MMMM yyyy")
            endStr = end.toString("dd MMMM yyyy")
        } else {
            val months = Months.monthsBetween(start, end)
            val monthDifference = months.months

            if (monthDifference != 0) {
                startStr = start.toString("dd MMMM")
                endStr = end.toString("dd MMMM")
            } else {
                startStr = start.toString("dd")
                endStr = end.toString("dd MMMM")
            }
        }
        return String.format("%s - %s", startStr, endStr)
    }

    /**
     * e.g.: 4:20 PM
     */
    fun shortTime(dateTime: DateTime): String {
        return shortTime(dateTime, Locale.getDefault())
    }

    /**
     * e.g.: 4:20 PM
     */
    fun shortTime(dateTime: DateTime, locale: Locale): String {
        return dateTime.toString(DateTimeFormat.shortTime().withLocale(locale).withZone(DateTimeZone.getDefault()))
    }

    /**
     * Utility to pair a unit (e.g. "minutes", "hours", "days") with a measurement. Returns `null` if the difference
     * exceeds the threshold.
     */
    fun unitAndDifference(initialSecondsDifference: Int, threshold: Int): Pair<String, Int>? {
        val secondsDifference = Math.abs(initialSecondsDifference)
        val daysDifference = Math.floor((secondsDifference / 86400).toDouble()).toInt()

        return when {
            secondsDifference < 3600 -> { // 1 hour
                val minutesDifference = Math.floor(secondsDifference / 60.0).toInt()
                Pair("minutes", minutesDifference)
            }
            secondsDifference < 86400 -> { // 24 hours
                val hoursDifference = Math.floor(secondsDifference.toDouble() / 60.0 / 60.0).toInt()
                Pair("hours", hoursDifference)
            }
            secondsDifference < threshold -> Pair("days", daysDifference)
            else -> null
        }
    }
}
