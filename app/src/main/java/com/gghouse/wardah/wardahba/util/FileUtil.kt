package com.gghouse.wardah.wardahba.util

import com.gghouse.wardah.wardahba.R
import com.gghouse.wardah.wardahba.WardahApp

class FileUtil {
    companion object {
        fun loadJSONFileToString(fileID: Int): String {
            return WardahApp.getInstance().resources.openRawResource(R.raw.user).bufferedReader().use { it.readText() };
        }
    }
}