package com.example.simplegamespads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplegamespads.databinding.FragmentGuessGameResultBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GuessGameResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuessGameResult : Fragment() {
    private var _binding: FragmentGuessGameResultBinding? = null
    private val binding: FragmentGuessGameResultBinding get() = _binding!!
    private val args: GuessGameResultArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuessGameResultBinding.inflate(inflater, container, false)

        binding.apply {
            // update the win-lose statement
            winLoseStatement.text =
                if (args.win) getString(R.string.win_label) else getString(R.string.lose_label)

            // action for the try again button
            tryAgainButton.setOnClickListener {
                root.findNavController().popBackStack()
            }

            // action for the ok button
            okButton.setOnClickListener {
                root.findNavController()
                    .navigate(
                        GuessGameResultDirections.actionGuessGameResultToGameMenu(
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