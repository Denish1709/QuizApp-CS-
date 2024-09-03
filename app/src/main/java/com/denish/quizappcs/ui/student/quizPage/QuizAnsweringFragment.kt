package com.denish.quizappcs.ui.student.quizPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denish.quizappcs.R
import com.denish.quizappcs.databinding.FragmentQuizAnsweringBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizAnsweringFragment : Fragment() {

    private lateinit var binding: FragmentQuizAnsweringBinding
    private val viewModel: QuizAnsweringViewModel by viewModels()
    private val args: QuizAnsweringFragmentArgs by navArgs()


    private var totalMark = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizAnsweringBinding.inflate(inflater, container, false)

        // Retrieve the quiz data based on the access code
        val accessCode = args.accessCode
        viewModel.fetchQuizData(accessCode)

        observeViewModel()

        btnSubmitAndNext()

        return binding.root
    }

    private fun btnSubmitAndNext() {
        binding.btnSubmit.setOnClickListener {
            submitQuestion()
        }

        binding.btnNext.setOnClickListener {
            nextQuestion()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer(seconds: Int) {

        countDownTimer?.cancel()

        if (seconds > 0) {
            binding.tvTimer.visibility = View.VISIBLE
            binding.tvTimer.text = "Time left: $seconds seconds"

            countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = millisUntilFinished / 1000
                    binding.tvTimer.text = "Time left: $secondsLeft seconds"
                }

                override fun onFinish() {
                    binding.tvTimer.text = "Time's up!"
                    disableAnswering()
                    binding.btnNext.isEnabled = true
                }
            }.start()

        } else {
            // Hide the timer if there is no time set for this question
            binding.tvTimer.visibility = View.GONE
        }
    }


    private fun disableAnswering() {
        binding.rgOptions.clearCheck()
        for (i in 0 until binding.rgOptions.childCount) {
            binding.rgOptions.getChildAt(i).isEnabled = false
        }
        binding.btnSubmit.isEnabled = false
    }

    private fun enableAnswering() {
        for (i in 0 until binding.rgOptions.childCount) {
            binding.rgOptions.getChildAt(i).isEnabled = true
        }
        binding.btnSubmit.isEnabled = true
    }

    private fun submitQuestion() {
        val selectedOptionId = binding.rgOptions.checkedRadioButtonId
        if (selectedOptionId == -1) {
            Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
        } else {
            val selectedRadioButton = binding.rgOptions.findViewById<RadioButton>(selectedOptionId)
            val selectedAnswer = selectedRadioButton.text.toString()

            val currentQuestionIndex = viewModel.currentQuestionIndex.value
            val correctAnswer = viewModel.quiz.value?.question?.get(currentQuestionIndex)?.correctAnswer

            if (selectedAnswer == correctAnswer) {
                binding.tvFeedback.text = "Correct!"
                binding.tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                totalMark++
            } else {
                binding.tvFeedback.text = "Incorrect. The correct answer is $correctAnswer."
                binding.tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            binding.tvFeedback.visibility = View.VISIBLE

            // Save the answer
            viewModel.submitAnswer(selectedAnswer)

            binding.btnNext.isEnabled = true
        }
    }

    private fun nextQuestion() {
        val currentQuestionIndex = viewModel.currentQuestionIndex.value
        val totalQuestions = viewModel.quiz.value?.question?.size ?: 0

        if (currentQuestionIndex + 1 < totalQuestions) {
            viewModel.moveToNextQuestion()
            binding.btnNext.isEnabled = false
            enableAnswering()
        } else {
            submitResult()
            findNavController().navigate(R.id.action_quizAnsweringFragment_to_rankingFragment)
            Toast.makeText(context, "You have successfully complete the Quiz", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitResult() {
        lifecycleScope.launch {
            val quizId = args.accessCode
            val totalMark = totalMark
            viewModel.addResult(quizId, totalMark)
        }
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                updateQuestions()
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.currentQuestionIndex.collect { index ->
                updateQuestions()
            }
        }
    }

    private fun updateQuestions() {
        val quiz = viewModel.quiz.value
        val currentQuestionIndex = viewModel.currentQuestionIndex.value
        val question = quiz?.question?.getOrNull(currentQuestionIndex)

        if (question != null) {
            binding.tvQuestion.text = question.question
            binding.rbOption1.text = question.option.getOrNull(0) ?: "Option 1"
            binding.rbOption2.text = question.option.getOrNull(1) ?: "Option 2"
            binding.rbOption3.text = question.option.getOrNull(2) ?: "Option 3"
            binding.rbOption4.text = question.option.getOrNull(3) ?: "Option 4"


            binding.rgOptions.clearCheck()
            binding.tvFeedback.visibility = View.INVISIBLE

            startTimer(question.time)

            if (currentQuestionIndex + 1 == quiz.question.size) {
                Toast.makeText(context, "You have reach to the last question of the quiz", Toast.LENGTH_SHORT).show()
                binding.btnNext.text = "Finish Quiz"
            } else {
                binding.btnNext.text = "Next"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}