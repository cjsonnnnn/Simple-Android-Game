package com.example.simplegamespads

import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class QuizzViewModel : ViewModel() {
    // attributes
    val trueQuestion: List<String> = listOf(
        "Sea otters have a favorite rock they use to break open food",
        "The blue whale is the biggest animal to have ever lived",
        "It takes a sloth two weeks to digest a meal",
        "Galapagos tortoises sleep up to 16 hours a day",
        "The most common blood type is 0- negative",
        "The human body is about 60% water",
        "Mount Everest is the tallest mountain in the world",
        "Greenland is the largest island in the world",
        "Toy Story was Pixar’s first movie",
        "Infants have more bones than adults"
    )
    val falseQuestions: List<String> = listOf(
        "Sharks are mammals",
        "Bats are blind",
        "A dog sweats by panting its tongue",
        "An ant can lift 1,000 times its body weight",
        "China has the longest coastline in the world",
        "An octopus has seven hearts",
        "South Africa has one capital",
        "A monkey was the first non-human to go into space",
        "The human body has four lungs",
        "Human skin regenerates every week"
    )
    var trueFalseIndiceResults: List<Int> = mutableListOf()
    var tobeShowingQuestionResults: List<String> = mutableListOf()
    val exclusive: Int = 10
    var curIteration: Int = 0
    var curCorrectAnswers: Int = 0
    var totalScore: Int = 0


    // methods
    fun generateRandomNumber(inclusive: Int = 1): Int {
        return Random.nextInt(inclusive, this.exclusive)
    }

    fun generateQuestionsAndIndices() {
        // determine the amount for both true and false questions
        var numOfTrueQuestions: Int = this.generateRandomNumber()
        var numOfFalseQuestions: Int = this.exclusive - numOfTrueQuestions

        // create a list that tells the true and false values, for example [1,0,1,1], which 1 tells the true and otherwise
        val truefalseindices =
            MutableList(numOfTrueQuestions) { 1 } + MutableList(numOfFalseQuestions) { 0 }

        // create a list of true and false questions in string
        val tobeshowingQuestions =
            this.trueQuestion.shuffled().take(numOfTrueQuestions) + this.falseQuestions.shuffled()
                .take(numOfFalseQuestions)

        // shuffle both the list in same order
        val zippedList = truefalseindices.zip(tobeshowingQuestions)
        val shuffledPairs = zippedList.shuffled()
        this.trueFalseIndiceResults = shuffledPairs.map { it.first }
        this.tobeShowingQuestionResults = shuffledPairs.map { it.second }
    }

    fun reset() {
        this.trueFalseIndiceResults = mutableListOf()
        this.tobeShowingQuestionResults = mutableListOf()
        this.curIteration = 0
        this.curCorrectAnswers = 0
        this.totalScore = 0
    }
}


class GuessViewModel : ViewModel() {
    // attributes
    val words: List<String> = listOf(
        "INDONESIA",
        "JAKARTA",
        "TABING",
        "CALVIN",
        "AMSTERDAM",
        "KOTLIN",
        "ANDROID",
        "BINDING",
        "LENOVO"
    )
    var selectedWord: String = ""
    private val _inputtedChar: MutableLiveData<Char> = MutableLiveData<Char>()
    val inputtedChar: LiveData<Char>
        get() = _inputtedChar
    val oneO = listOf(1, 0)
    var numEmpty: Int = 0
    var numChances: Int = 0
    var gameOver: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isWin: Boolean = false
    var lostChars = mutableListOf<Char>()
    var lostIndices = mutableListOf<Int>()
    var textViews = mutableListOf<TextView>()
    var isShownIndices: MutableList<Int> = mutableListOf()


    // to generate a word from list of words, which will be used as the question
    fun generateSelectedWord() {
        this.selectedWord = this.words.shuffled().take(1)[0]
    }

    // to generate/fill the isShownIndices that tells whether or not for each char will be shown
    fun generateShownIndices() {
        this.apply {
            for (i in 0 until selectedWord.length) {
                // determine whether the char is going to be shown or not
                val isShow: Boolean = isCharShowed(i)
                if (isShow) isShownIndices.add(i)
            }
        }
    }

    // to assign the numChances value with custom setting
    fun generateNumOfChances() {
        this.numChances = (this.numEmpty * 0.5).toInt() + 1
    }

    // to determine whether or not a char will be shown
    fun isCharShowed(i: Int): Boolean {
        this.apply {
            if ((oneO.shuffled()
                    .take(1)[0] == 1 || (numEmpty > selectedWord.length / 2)) && (numEmpty != 0)
            ) {
                return true
            } else {
                numEmpty++
                lostChars.add(selectedWord[i])
                lostIndices.add(i)
                return false
            }
        }
    }

    // to reset all attributes in order to reset the game
    fun reset() {
        this.apply {
            selectedWord = ""
            numEmpty = 0
            numChances = 0
            gameOver = MutableLiveData<Boolean>()
            lostChars = mutableListOf<Char>()
            lostIndices = mutableListOf<Int>()
            textViews = mutableListOf<TextView>()
            isShownIndices = mutableListOf()
        }
    }
}