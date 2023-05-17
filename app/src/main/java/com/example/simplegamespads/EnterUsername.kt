package com.example.simplegamespads

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.simplegamespads.databinding.FragmentEnterUsernameBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EnterUsername.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterUsername : Fragment() {
    private var _binding: FragmentEnterUsernameBinding? = null
    private val binding: FragmentEnterUsernameBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEnterUsernameBinding.inflate(inflater, container, false)

        // implement binding
        binding.apply {
            // action when submit
            usernameSubmitButton.setOnClickListener {
                // get action and navigate to it
                val action =
                    EnterUsernameDirections.actionEnterUsernameToGameMenu(
                        username.text.toString()
                    )
                username.text = null
                root.findNavController().navigate(action)
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


// for making the current value inside editText is not disappear
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("usernameOnWrite", binding.username.text.toString())
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val binding = FragmentEnterUsernameBinding.bind(requireView())
//        binding.username.setText(savedInstanceState?.getString("usernameOnWrite"))
//    }