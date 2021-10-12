package com.zmt.kotlin_wla.mvp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zmt.kotlin_wla.R
import com.zmt.kotlin_wla.mvp.model.QuizQuestion
import com.zmt.kotlin_wla.mvp.presenter.IConfigPresenter
import com.zmt.kotlin_wla.mvp.presenter.ConfigPresenter

/**
 * Configuration Activity to define the parameters of the game
 */
class MainActivity : IConfigView, AppCompatActivity() {

    private lateinit var mConfigPresenter : IConfigPresenter

    private lateinit var mSelectGameTypeText : TextView
    private lateinit var mSelectCategoryText : TextView
    private lateinit var mSelectDifficultyText : TextView

    private lateinit var mStartGameButton : Button
    private lateinit var mLoadingScreen : FrameLayout

    private lateinit var mGameTypeErrorText : TextView
    private lateinit var mCategoryErrorText : TextView
    private lateinit var mDifficultyErrorText : TextView

    private lateinit var DEFAULT_PARAM : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mConfigPresenter = ConfigPresenter(this, resources.getInteger(R.integer.total_questions))
        DEFAULT_PARAM = resources.getString(R.string.select_an_option)
        initViews()
    }

    /**
     * Initialize the view elements with the correct values
     */
    private fun initViews(){
        mSelectGameTypeText     = findViewById(R.id.txt_select_game_type)
        mSelectCategoryText     = findViewById(R.id.txt_select_category)
        mSelectDifficultyText   = findViewById(R.id.txt_select_difficulty)

        // Declare the action when a parameter is selected to be modified
        val onSelectParameter = View.OnClickListener {
            when(it.id) {
                mSelectGameTypeText.id      -> runParameterIntent("Game Type",      resources.getStringArray(R.array.game_types).toCollection(ArrayList()), ConfigPresenter.GAME_TYPE_PARAMETER)
                mSelectCategoryText.id      -> runParameterIntent("Game Category",  resources.getStringArray(R.array.categories).toCollection(ArrayList()), ConfigPresenter.CATEGORY_PARAMETER)
                mSelectDifficultyText.id    -> runParameterIntent("Game Difficulty",resources.getStringArray(R.array.game_difficulties).toCollection(ArrayList()), ConfigPresenter.DIFFICULTY_PARAMETER)
                else -> Log.e("ALL", "Unable to detect the selected parameter")
            }
        }

        // Bind the select parameter action to the configurable texts
        mSelectGameTypeText.setOnClickListener(onSelectParameter)
        mSelectCategoryText.setOnClickListener(onSelectParameter)
        mSelectDifficultyText.setOnClickListener(onSelectParameter)

        // Fetch the start game button
        mStartGameButton = findViewById(R.id.btn_start_game)

        // Bind the button action
        mStartGameButton.setOnClickListener{
            // Call the presenter
            mConfigPresenter.onStartGamePressed(
                mSelectCategoryText.text.toString(),
                mSelectDifficultyText.text.toString(),
                mSelectGameTypeText.text.toString()
            )
        }

        // Fetch the error text views
        mGameTypeErrorText = findViewById(R.id.txt_error_game_type)
        mDifficultyErrorText = findViewById(R.id.txt_error_difficulty)
        mCategoryErrorText = findViewById(R.id.txt_error_category)
        mLoadingScreen = findViewById(R.id.loading_screen_fragment)
    }

    /**
     * Method to trigger the ListSelection of the given dataset.
     * Each parameter will provide a different dataset and will listen for the response in the onActivityResult method.
     */
    private fun runParameterIntent(pTitle: String, pList: ArrayList<String>, pParamCode : Int) {
        // Declare the Intent
        val intent = Intent(this, SelectableListActivity::class.java)
        // Declare the data bundle
        val bundle = Bundle()
        // Bind the type of parameter we are currently setting
        bundle.putString("title", pTitle)
        // Bind the list data into the bundle
        bundle.putStringArrayList("list", pList)
        // Inform the Selection list which parameter are we modifying
        bundle.putInt("paramCode", pParamCode)
        // Attach the data bundle into the intent
        intent.putExtras(bundle)
        // Run the intent waiting for the result
        startActivityForResult(intent, ConfigPresenter.PARAMETER_INTENT_CODE)
    }

    /**
     * Listens to a ListSelection Activity Result and manages the repsonse.
     * Updates the values of the view if required
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Call the parent implementation
        super.onActivityResult(requestCode, resultCode, data)

        // Ensure the result was valid
        if (resultCode == Activity.RESULT_OK) {
            // Get the parameter which has been successfully modified
            when(data?.getIntExtra("paramCode", 0)) {
                // Game type parameter
                ConfigPresenter.GAME_TYPE_PARAMETER     -> {
                    mSelectGameTypeText.text = data.getStringExtra("result")
                    hideGameTypeErrorText()
                }
                // Game category parameter
                ConfigPresenter.CATEGORY_PARAMETER      ->  {
                    mSelectCategoryText.text = data.getStringExtra("result")
                    hideCategoryErrorText()
                }
                // Game difficulty parameter
                ConfigPresenter.DIFFICULTY_PARAMETER    -> {
                    mSelectDifficultyText.text = data.getStringExtra("result")
                    hideDifficultyErrorText()
                }
            }
        }
    }

    /**
     * Builds and runs the Intent from the configuration screen to the Game Screen with the required game data
     */
    override fun runGameIntent(data : ArrayList<QuizQuestion>) {
        // Declare the Game Intent
        val intent = Intent(this, GameActivity::class.java)
        // Declare the data bundle
        val bundle = Bundle()
        // Bind the game data into the bundle
        bundle.putParcelableArrayList("quizData", data)
        // Bind the game parameters into the bundle
        bundle.putString("type", mSelectGameTypeText.text.toString())
        bundle.putString("category", mSelectCategoryText.text.toString())
        bundle.putString("difficulty", mSelectDifficultyText.text.toString())
        // Attach the data bundle into the intent
        intent.putExtras(bundle)
        // Run the intent
        startActivity(intent)
    }

    /**
     * Method to run a runnable in the main UI Thread. Used for asynchronous operations
     */
    override fun runOnMainUIThread(pRunnable: Runnable) { runOnUiThread(pRunnable) }

    /**
     * Displays or hides the Loading Screen fragment
     */
    override fun displayLoadingScreen(pShow: Boolean) {
        supportFragmentManager.beginTransaction()
            .add(R.id.loading_screen_fragment, Fragment(R.layout.loading_view))
            .addToBackStack(null)
            .commit()

        if(pShow)
            mLoadingScreen.visibility = View.VISIBLE
        else
            mLoadingScreen.visibility = View.GONE
    }

    /**
     * Displays a game type error when non defined
     */
    override fun displayGameTypeError(pMessage: String) {
        mGameTypeErrorText.text = pMessage
        mGameTypeErrorText.visibility = View.VISIBLE
    }

    /**
     * Displays a game category error when non defined
     */
    override fun displayCategoryError(pMessage: String) {
        mCategoryErrorText.text = pMessage
        mCategoryErrorText.visibility = View.VISIBLE
    }

    /**
     * Displays a game difficulty error when non defined
     */
    override fun displayDifficultyError(pMessage: String) {
        mDifficultyErrorText.text = pMessage
        mDifficultyErrorText.visibility = View.VISIBLE
    }

    /**
     * Displays a general error using Toast library
     */
    override fun displayGeneralError(pMessage: String) {
        Toast.makeText(this, pMessage, Toast.LENGTH_LONG).show()
    }

    /**
     * Hides the game type error text
     */
    override fun hideGameTypeErrorText() {
        mGameTypeErrorText.visibility = View.GONE
    }

    /**
     * Hides the game difficulty error text
     */
    override fun hideDifficultyErrorText() {
        mDifficultyErrorText.visibility = View.GONE
    }

    /**
     * Hides the game category error text
     */
    override fun hideCategoryErrorText() {
        mCategoryErrorText.visibility = View.GONE
    }
}
