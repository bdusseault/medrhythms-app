package com.github.bdusseault.medrhythms_app.helpers

import java.lang.Exception

/**
 * Exception for any playlist-related operations
 * @author Bret Dusseault
 * @since 9/6/2021
 * @constructor Creates an exception with the given message
 */
class PlaylistException(private val error: String) : Exception(error)
{
    /**
     * Creates an exception with no message
     */
    constructor() : this("")
}