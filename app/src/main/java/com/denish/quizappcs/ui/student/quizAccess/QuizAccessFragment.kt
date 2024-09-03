package com.denish.quizappcs.ui.student.quizAccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.ui.student.quizPage.QuizAnsweringFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuizAccessFragment : Fragment() {

    private val viewModel: QuizAccessViewModel by viewModels()

    private var accessCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_access, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStartQuiz: Button = view.findViewById(R.id.btn_start_quiz)
        val editTextAccessCode: EditText = view.findViewById(R.id.et_access_code)

        btnStartQuiz.setOnClickListener {
            accessCode = editTextAccessCode.text.toString()

            if (accessCode.isNotEmpty()) {
                viewModel.verifyAccessCode(accessCode)
            } else {
                Toast.makeText(requireContext(), "Please enter a Quiz ID", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.quizExists.collect { exists ->
                when (exists) {
                    true -> {
                        val action = QuizAccessFragmentDirections
                            .actionQuizAccessFragmentToQuizAnsweringFragment(accessCode)
                        findNavController().navigate(action)

                    }
                    false -> {
                        Toast.makeText(requireContext(), "Quiz ID does not exist", Toast.LENGTH_SHORT).show()
                    }
                    null -> {
                        Toast.makeText(requireContext(), "Checking Quiz ID...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}