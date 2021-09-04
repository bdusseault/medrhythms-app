package com.github.bdusseault.medrhythms_app.helpers

import java.lang.Exception

class PlaylistException(private val error: String) : Exception(error)
{
    constructor() : this("")
}