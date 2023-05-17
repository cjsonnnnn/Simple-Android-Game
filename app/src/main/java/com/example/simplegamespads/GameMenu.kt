package com.example.simplegamespads

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplegamespads.databinding.FragmentGameMenuBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GameMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMenu : Fragment() {
    private var _binding: FragmentGameMenuBinding? = null
    private val binding: FragmentGameMenuBinding get() = _binding!!
    private val args: GameMenuArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameMenuBinding.inflate(inflater, container, false)

        // implement binding
        binding.apply {
            // get username and show it to say welcome
            welcomeUsernameLabel.text =
                if (args.username.equals("")) "Welcome!!" else "Welcome, ${args.username}!!"

            // set the onClick for the first game
            firstGameMenu.setOnClickListener {
                // define navigation controller
                root.findNavController()
                    .navigate(GameMenuDirections.actionGameMenuToQuizzGamePlaying())
            }

            // set the onClick for the second game
            secondGameMenu.setOnClickListener {
                root.findNavController()
                    .navigate(GameMenuDirections.actionGameMenuToGuessGamePlaying())
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