package com.example.simplegamespads

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplegamespads.databinding.FragmentQuizzGameResultBinding

/**
 * A simple [Fragment] subclass.
 * Use the [QuizzGameResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizzGameResult : Fragment() {
    private var _binding: FragmentQuizzGameResultBinding? = null
    private val binding: FragmentQuizzGameResultBinding get() = _binding!!
    private val args: QuizzGameResultArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuizzGameResultBinding.inflate(inflater, container, false)

        // implement binding apply
        binding.apply {
            // define the text view
            scoreTextView.text = args.score.toString()

            // define whether the user is win or lose
            winLoseTv.text = if (args.score > 63) "You Win!!" else "You Lose!!"

            // action to try again button
            tryAgainButton.setOnClickListener {
                root.findNavController().popBackStack()
            }

            // action to ok button
            okButton.setOnClickListener {
                root.findNavController()
                    .navigate(
                        QuizzGameResultDirections.actionQuizzGameResultToGameMenu(
                            ""
                        )
                    )
            }

            return root
        }
    }

    // to set back the value of `_binding` variable to null again
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}