package com.denish.quizappcs.ui.teacher.edit

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.core.utils.CSVUtil
import com.denish.quizappcs.data.model.quiz.Question
import com.denish.quizappcs.databinding.FragmentManageQuestionBinding
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.teacher.add.AddQuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class EditQuestionFragment : BaseFragment<FragmentManageQuestionBinding>() {

    override val viewModel: EditQuestionViewModel by viewModels()

    private var selectedQuestions: List<Question>? = null
    override fun getLayoutResource() = R.layout.fragment_manage_question

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvAdd?.text = "Edit Question"

        binding?.btnSubmitQuiz?.text = "Save Edit"

        val csvFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                lifecycleScope.launch {
                    val questions = CSVUtil.parseCSV(requireContext().contentResolver.openInputStream(uri)!!)
                    selectedQuestions = questions
                    // Update UI or show a message indicating the CSV was uploaded
                    binding?.etTextSelectedFileName?.text = "CSV file uploaded with ${questions.size} questions"

                }
            }
        }

        binding?.etUploadCSV?.setOnClickListener {
            csvFileLauncher.launch("text/*")
        }

        binding?.btnSubmitQuiz?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val quizId = binding?.etAccessCode?.text.toString()
            val desc = binding?.etDesc?.text.toString()
            val questions = selectedQuestions ?: viewModel.quiz.value?.question ?: emptyList()
            val publishDate = getCurrentDate()

            val isUpdatedSuccessfully = viewModel.updateQuiz(questions, title, quizId, desc, publishDate)

            if (isUpdatedSuccessfully) {
                // If the update is successful, show a success message and navigate
                Toast.makeText(requireContext(), R.string.quizUpdateSuccess, Toast.LENGTH_SHORT).show()
                findNavController().navigate(EditQuestionFragmentDirections.actionEditQuestionFragmentToDashBoardFragment())
            } else {
                // If the update fails, show an error message
                Toast.makeText(requireContext(), R.string.quizUpdateFailed, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.quiz.collect{ quiz ->
                 quiz?.let{
                    Log.d("debugging", it.toString())
                    binding?.etTitle?.setText(it.title)
                    binding?.etAccessCode?.setText(it.accessCode)
                    binding?.etDesc?.setText(it.desc)
                }
            }
        }
    }

}