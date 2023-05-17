package com.example.simplegamespads

import android.content.res.Resources
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplegamespads.databinding.FragmentGuessGamePlayingBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GuessGamePlaying.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuessGamePlaying : Fragment() {
    private var _binding: FragmentGuessGamePlayingBinding? = null
    private val binding: FragmentGuessGamePlayingBinding get() = _binding!!
    private lateinit var viewModel: GuessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuessGamePlayingBinding.inflate(inflater, container, false)  // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(GuessViewModel::class.java)     // define the viewModel
        val tag = "DEBUG GG"


        // some local functions regarding view processing
        fun Int.dpToPx(): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }             // to convert dp to px unit

        fun generateTheTextView(                // to generate a custom text view
            isShown: Boolean, i: Int, textView: TextView
        ): TextView {
            // define some linear layout parameters
            var layoutParamsObj = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                weight = 1f
                marginStart = 3.dpToPx()
                marginEnd = 3.dpToPx()
            }

            // create default textView object and setting it up
            var textView = textView.apply {
                layoutParams = layoutParamsObj
                setBackgroundResource(R.drawable.textview_border_bottom)
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 54f)
                setPadding(0, 0, 0, 7.dpToPx())
                viewModel.apply {
                    text = if (isShown) selectedWord[i].toString() else ""
                }
            }

            return textView
        }

        fun generateQuestionTextView() {
            binding.apply {
                viewModel.apply {
                    textViews = mutableListOf<TextView>()
                    var tV: TextView = TextView(requireContext())
                    for (i in 0 until selectedWord.length) {
                        if (i in isShownIndices) {
                            tV = generateTheTextView(true, i, TextView(requireContext()))
                            textViewsContainer.addView(tV)
                        } else {
                            tV = generateTheTextView(false, i, TextView(requireContext()))
                            textViewsContainer.addView(tV)
                        }
                        textViews.add(tV)
                    }
                }
            }
        }    // to generate text view for each character


        // implement binding apply
        binding.apply {
            viewModel.apply {
                // define data binding
                guessVM = viewModel
                lifecycleOwner = this@GuessGamePlaying

                if (selectedWord.equals("")) {      // condition when question is not generated yet
                    generateSelectedWord()          // generate a word to be selected
                    generateShownIndices()          // generate a list of char shown status
                    generateQuestionTextView()      // generate the views
                    generateNumOfChances()          // determine number of chances to be given based on number of empty textViews

                } else {                            // condition when question in model is already generated
                    generateQuestionTextView()      // generate the views
                }

                // update statement on the screen that tells num of chances left
                numChancesView.text =
                    if (numChances == 1) "1 chance left" else "$numChances chances left"

                // set some observers
                gameOver.observe(viewLifecycleOwner) {
                    viewModel.reset()       // reset viewModel
                    guessBox.text = null    // reset guess box
                    root.findNavController().navigate(      // navigate to the result page
                        GuessGamePlayingDirections.actionGuessGamePlayingToGuessGameResult(
                            isWin
                        )
                    )
                }

                // action when clicking the guess button
                guessButton.setOnClickListener {
                    try {
                        var theChar: Char = guessBox.text.toString()[0]
                        if (theChar in lostChars) {     // condition when the guessed char is correct
                            Toast.makeText(requireContext(), "you're correct!", Toast.LENGTH_SHORT)
                                .show()
                            textViews[lostIndices[lostChars.indexOf(theChar)]].text =
                                theChar.toString()
                            isShownIndices.add(lostIndices[lostChars.indexOf(theChar)])
                            lostIndices.removeAt(lostChars.indexOf(theChar))
                            lostChars.remove(theChar)

                            if (lostChars.isEmpty()) {  // condition when the game is over, and the user wins the game
                                isWin = true; gameOver.value = true;
                            }
                            guessBox.text = null        // reset guess box
                        } else {                        // condition when the guessed char is wrong
                            Toast.makeText(requireContext(), "you're wrong!", Toast.LENGTH_SHORT)       // create a toast message
                                .show()
                            numChances--
                            if (numChances == 0) {      // condition when the game is over, and user loses the game
                                isWin = false; gameOver.value = true
                            }
                            numChancesView.text =       // update statement on the screen that tells num of chances left
                                if (numChances == 1) "1 chance left" else "$numChances chances left"
                            guessBox.text = null        // reset guess box
                        }
                    } catch (e: Exception) {
                        Log.d(tag, "please input a char into the guess box")
                    }
                }

                return root
            }
        }
    }

    // to set back the value of `_binding` variable to null again
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}