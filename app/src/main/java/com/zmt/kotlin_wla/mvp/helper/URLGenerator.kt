package com.zmt.kotlin_wla.mvp.helper

import java.lang.Exception

/**
 * Helper class to build an specific URL for Trivia API and fetch the quiz data given the desired user configuration
 */
object URLGenerator {

    /* Private Member Variables */
    private val BASE_URL        = "https://opentdb.com/api.php?amount="
    private val DIFFICULTY_TAG  = "difficulty"
    private val CATEGORY_TAG    = "category"
    private val GAME_TYPE_TAG   = "type"

    /* Difficulties Map */
    private val difficultiesMap : Map<String, String> = mapOf(
        "Easy" to "easy",
        "Medium" to "medium",
        "Hard" to "hard"
    )

    /* Categories Map */
    private val categoriesMap : Map<String, Int> = mapOf(
        "General Knowledge" to 9,
        "Entertainment: Books" to 10,
        "Entertainment: Films" to 11,
        "Entertainment: Music" to 12,
        "Entertainment: Musicals & Theatres" to 13,
        "Entertainment: Television" to 14,
        "Entertainment: Video Games" to 15,
        "Entertainment: Board Games" to 16,
        "Science & Nature" to 17,
        "Science: Computers" to 18,
        "Science: Mathematics" to 19,
        "Mythology" to 20,
        "Sports" to 21,
        "Geography" to 22,
        "History" to 23,
        "Politics" to 24,
        "Art" to 25,
        "Celebrities" to 26,
        "Animals" to 27,
        "Vehicles" to 28,
        "Entertainment: Comics" to 29,
        "Science: Gadgets" to 30,
        "Entertainment: Japanese Anime & Manga" to 31,
        "Entertainment: Cartoon & Animations" to 32
    )

    /* Game Types Map */
    private val gameTypesMap : Map<String, String> = mapOf(
        "Multiple choice" to "multiple",
        "True or False" to "boolean"
    )

    /**
     * Constructs the URL to query given a set of game quiz configurations
     * driven by category, difficulty and game type
     */
    fun getUrl(pCategory : String, pDifficulty : String, pGameType : String, pTotalQuestions: Int) : String {
        // Get the category Id
        val categoryId : Int = try {
            categoriesMap[pCategory]!!
        }catch (e : Exception){
            -1
        }

        // Get the difficulty url name
        val difficulty : String = difficultiesMap[pDifficulty]!!

        // Get the game type
        val type : String = gameTypesMap[pGameType]!!

        // Construct the URL
        var url = "$BASE_URL$pTotalQuestions&"

        // Add the category URL parameter
        url += if(categoryId != -1) "$CATEGORY_TAG=$categoryId&" else ""

        // Add the difficulty URL parameter
        url += "$DIFFICULTY_TAG=$difficulty&"

        // Add the game type URL parameter
        url += "$GAME_TYPE_TAG=$type"

        // Return the final URL
        return url
    }
}