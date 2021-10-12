package com.zmt.kotlin_wla.mvp.interactor

import org.json.JSONArray

/**
 * Interface to manage the callback from an HTTP Asynchronous request
 */
interface IRequestCallback {
    fun onSuccess(pResponse : JSONArray)
    fun onFailure(pError : String)
}