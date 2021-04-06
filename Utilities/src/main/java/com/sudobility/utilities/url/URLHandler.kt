package com.sudobility.utilities.url

import java.net.URL


public interface URLHandlerProtocol {
    fun open(url: URL, completion: ((Boolean) -> Unit)?)
    fun canOpenURL(url: URL) : Boolean
}

public class URLHandler {
    companion object {
        var shared: URLHandlerProtocol? = null
    }
}
