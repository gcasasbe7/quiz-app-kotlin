package com.zmt.kotlin_wla.mvp.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.presenter.IGameParameterListener
import com.zmt.kotlin_wla.mvp.view.adapter.SelectableListAdapter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Activity class to display a set of String data in list format, and allow a selection of one of the items.
 * Also provides the ability to search through the list
 */
class SelectableListActivity : AppCompatActivity() {

    private lateinit var mTitleValue: String
    private lateinit var mListContent: ArrayList<String>
    private lateinit var mFilteredListContent: ArrayList<String>
    private lateinit var mParameterListener: IGameParameterListener

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mTitle : TextView
    private lateinit var mCloseIcon : ImageView
    private lateinit var mSearch: EditText

    private var mParamCode : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectable_list)
        initViews()
        fetchIntentData()
    }

    /**
     * Fetches all the required data from the Bundle pack and initializes the view elements
     */
    private fun fetchIntentData() {
        // Get the bundle pack and extract the data
        this.mListContent   = this.intent.extras?.getStringArrayList("list")!!
        this.mTitleValue    = this.intent.extras?.getString("title")!!
        this.mParamCode     = this.intent.extras?.getInt("paramCode")!!

        // Initialize the filtered list as the normal list
        this.mFilteredListContent = this.mListContent.toCollection(ArrayList())

        // Set the values
        this.mTitle.text = this.mTitleValue
        // Update the search hint with the current parameter
        mSearch.hint = "Search a ${mTitleValue.toLowerCase(Locale.ROOT)}"
        // Set the Recycler View Layout Manager
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        // Set the dividers
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        // Declare the functionality
        mParameterListener = object : IGameParameterListener {
            // Successful selection
            override fun onOptionSelected(pSelectedOption: String) { endWithResult(pSelectedOption) }
            // User has exit the screen
            override fun onExit() { finish() }
        }
        // Refresh the adapter with the content and logic
        updateAdapter(mListContent, mParameterListener)
    }

    /**
     * Updates the adapter and notifies it to refresh the rendeering process
     */
    private fun updateAdapter(mDataset : ArrayList<String>, mListener : IGameParameterListener){
        // Set the list adapter
        mRecyclerView.adapter = SelectableListAdapter(mDataset, mListener)
        // Notify the adapter a new dataset has been provided
        (mRecyclerView.adapter as SelectableListAdapter).notifyDataSetChanged()
    }

    /**
     Fetches the view pointers of the activity and initializes it's values and actions
     */
    private fun initViews() {
        // Fetch the view pointers
        mRecyclerView = findViewById(R.id.main_list)
        mTitle = findViewById(R.id.txt_selectable_list_title)
        mCloseIcon = findViewById(R.id.ic_close)
        mSearch = findViewById(R.id.et_list_search)

        // Bind the action to the cross icon
        mCloseIcon.setOnClickListener { finish() }

        // Bind the search methodology
        mSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            // Not used
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Ends the activity providing a result depending on the user selection.
     * The Main configuration screen will be listening for this result and will manage the interaction
     */
    private fun endWithResult(pResult : String) {
        // Declare the return intent
        val returnIntent = Intent()
        // Attach the result to the bundle
        returnIntent.putExtra("paramCode", mParamCode)
        // Attach the result to the bundle
        returnIntent.putExtra("result", pResult)
        // Assert the result is valid
        setResult(Activity.RESULT_OK, returnIntent)
        // Kill the activity
        finish()
    }

    /**
     * Basic filtering method for the main dataset.
     * Filtering methodology: startsWith() OR contains() ignoring case sensitivity
     */
    private fun filter(pPattern : String) {
        // Clear the previous results prior starting the filtering
        mFilteredListContent.clear()

        // Loop the main dataset
        for(iElement : String in mListContent) {
            // Filtering methodology
            if ( iElement.startsWith(pPattern, ignoreCase = true) || iElement.contains(pPattern, ignoreCase = true)) {
                mFilteredListContent.add(iElement)
            }
        }

        // Update the adapter with the filtered results
        updateAdapter(mFilteredListContent, mParameterListener)
    }
}