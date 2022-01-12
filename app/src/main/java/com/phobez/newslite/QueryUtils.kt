package com.phobez.newslite

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

object QueryUtils {

    private fun createUrl(stringUrl: String): URL? {
        var url: URL? = null

        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Timber.e(e)
        }

        return url
    }

    suspend fun makeHttpRequest(stringUrl: String): String {
        return makeHttpRequest(createUrl(stringUrl))
    }

    suspend fun makeHttpRequest(url: URL?): String {
        var jsonResp = ""

        if (url == null) return jsonResp

        return withContext(Dispatchers.IO) {
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null

            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResp = readFromStream(inputStream)
                } else {
                    Timber.e("Error ${urlConnection.responseCode}")
                }

            } catch (e: IOException) {
                Timber.e(e)
            } finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }

            jsonResp
        }
    }

    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()

        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()

            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }

        return output.toString()
    }
}