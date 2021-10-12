package com.zmt.kotlin_wla.mvp.interactor

import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Logic class to define the protocol behind the HTTP Request to the Trivia Public API.
 * Subclassing from AsyncTask and managing the response communicated via callback
 */
class HTTPRequestTask(callback: IRequestCallback) : AsyncTask<String, String, String>() {

    private val RESPONSE_CODE_TAG       = "response_code"
    private val RESPONSE_RESULTS_TAG    = "results"
    private val SUCCESSFUL_RESPONSE     = 0
    private val NO_RESULTS              = 1

    // Callback instance
    private val mCallback : IRequestCallback = callback

    /**
     * Define the asynchronous task logic
     */
    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 7000

        return convertStreamToString(urlConnection.inputStream)
    }

    /**
     * Define the asynchronous task data management once the request is done
     */
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        // Parse the response into a JSON Object
        val jsonResponse = JSONObject(result!!)
        val responseCode = jsonResponse.get(RESPONSE_CODE_TAG)
        val responseData = jsonResponse.get(RESPONSE_RESULTS_TAG) as JSONArray

        // Check the response code and the data validity
        if (responseCode == SUCCESSFUL_RESPONSE && responseData.length() > 0) {
            // Success! Return the data in JSONArray format
            mCallback.onSuccess(responseData)
        } else {
            // Failure... return a convenient error message
            mCallback.onFailure("No quiz questions could be found with your settings. Please try again with different ones")
        }
    }

    /**
     * Private method to translate the data received from the HTTP Request.
     * Reads the given InputStream and extracts all the data.
     */
    private fun convertStreamToString(inputStream : InputStream) : String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line : String
        var complete : String = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null){
                    complete += line
                }
            } while (line != null)

            inputStream.close()

        } catch (ex : Exception){
            ex.printStackTrace()
        }

        return complete
    }
}
