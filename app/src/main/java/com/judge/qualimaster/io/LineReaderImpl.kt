package com.judge.qualimaster.io

import com.judge.core.io.LineReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class LineReaderImpl(inputStream: InputStream) : LineReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): String? {
        return reader.readLine()
    }
}