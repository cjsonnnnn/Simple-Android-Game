package com.example.simplegamespads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.simplegamespads.databinding.FragmentQuizzGamePlayingBinding

/**
 * A simple [Fragment] subclass.
 * Use the [QuizzGamePlaying.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizzGamePlaying : Fragment() {
    private var _binding: FragmentQuizzGamePlayingBinding? = null
    private val binding: FragmentQuizzGamePlayingBinding get() = _binding!!
    private lateinit var viewModel: QuizzViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuizzGamePlayingBinding.inflate(inflater, container, false)

        // define the viewModel, (even though... the declaration is not gonna override the previous object)
        // Because it creates an instance of the ViewModel scoped to the Fragment's view. This instance is retained across configuration changes (e.g., screen rotations) and is managed by the ViewModelProvider system.
        viewModel = ViewModelProvider(this).get(QuizzViewModel::class.java)

        binding.apply {
            viewModel.apply {
                // generate the list just if the lists are not generated yet
                if (viewModel.trueFalseIndiceResults.isEmpty()) generateQuestionsAndIndices()

                // define text view and show the first question
                questions.text = tobeShowingQuestionResults[curIteration]

                // function that represents the game flow
                fun quizzGameSystem(answerState: Int) {
                    if (trueFalseIndiceResults[curIteration] == answerState) {
                        curCorrectAnswers += 1
                        Toast.makeText(requireContext(), "you're correct!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "you're wrong!", Toast.LENGTH_SHORT).show()
                    }
                    curIteration++

                    if (curIteration < exclusive) {     // condition when there is still question
                        // update the text view with next question
                        questions.text = tobeShowingQuestionResults[curIteration]
                    } else {                            // condition when there is no question anymore
                        // count the total score
                        totalScore = ((curCorrectAnswers.toDouble() / exclusive) * 100).toInt()

                        // navigate to result fragment
                        val action =
                            QuizzGamePlayingDirections.actionQuizzGamePlayingToQuizzGameResult(
                                totalScore
                            )

                        // reset viewModel
                        viewModel.reset()

                        // navigate
                        root.findNavController().navigate(action)
                    }
                }

                // define actions for both true and false buttons
                trueButton.setOnClickListener {
                    quizzGameSystem(1)
                }
                falseButton.setOnClickListener {
                    quizzGameSystem(0)
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