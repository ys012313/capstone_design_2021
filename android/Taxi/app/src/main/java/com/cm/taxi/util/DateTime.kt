package com.cm.taxi.util

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.util.Locale


val LocalDate.latestYearMonthFormat: String
    get() = "${year}년${(monthValue)}월"



fun LocalDate.remainDay(day: String): Long {
    return ChronoUnit.DAYS.between(this, LocalDate.of(year, monthValue, day.toInt()))
}


internal fun LocalDate.boardingDate(day: String, hour: String, minute: String): String {
    val target = LocalDate.of(year, month, day.toInt()).atTime(hour.toInt(),minute.toInt())
    return DateTimeFormatter.ofPattern("yyyyMMddHHmm", Locale.KOREAN).format(target)
}

