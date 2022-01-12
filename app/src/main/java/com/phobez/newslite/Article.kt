package com.phobez.newslite

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Article(
    private var topic_: String,
    private val date_: String,
    private var title_: String,
    private var link_: String,
    private val contributors_: List<String>
) {
    val topic: String get() = topic_
    val title: String get() = title_
    val link: String get() = link_

    fun getInfo(): String {
        val contributors = getContributors()
        if (contributors.isBlank()) return getDate()
        return "${getDate()} | ${getContributors()}"
    }

    private fun getDate(): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN).withZone(ZoneId.systemDefault())
        val instant = Instant.parse(date_)
        return formatter.format(instant)
    }

    private fun getContributors(): String {
        return when (contributors_.count()) {
            0 -> ""
            1 -> contributors_[0]
            2 -> contributors_.joinToString(separator = "&")
            else -> contributors_.joinToString(separator = ", ")
        }
    }

    companion object {
        private const val DATE_PATTERN = "dd MMM yy"
    }
}