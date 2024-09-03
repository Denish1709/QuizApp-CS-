package com.denish.quizappcs.ui.teacher.add

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denish.quizappcs.R
import com.denish.quizappcs.core.utils.CSVUtil
import com.denish.quizappcs.databinding.FragmentAddQuestionBinding
import com.denish.quizappcs.databinding.FragmentManageQuestionBinding
import com.denish.quizappcs.ui.base.BaseFragment
import com.denish.quizappcs.ui.teacher.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddQuestionFragment : BaseFragment<FragmentManageQuestionBinding>() {

    override val viewModel: AddQuestionViewModel by viewModels()

    private val csvFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            lifecycleScope.launch {
                loadQuestionsFromCSV(it)
                updateFileName(it)
            }
        }
    }
    override fun getLayoutResource() = R.layout.fragment_manage_question

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.etUploadCSV?.setOnClickListener {
            openFileChooser()
        }

    }

    private fun openFileChooser() {
        csvFileLauncher.launch("text/*")
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private suspend fun loadQuestionsFromCSV(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        inputStream?.let {
            val questions = CSVUtil.parseCSV(requireContext().contentResolver.openInputStream(uri)!!)
            binding?.btnSubmitQuiz?.setOnClickListener {
                val title = binding?.etTitle?.text.toString()
                val accessCode = binding?.etAccessCode?.text.toString()
                val desc = binding?.etDesc?.text.toString()
                val publishDate = getCurrentDate()

                viewModel.submitQuestion(questions, title, accessCode, desc, publishDate)
                Log.d("BaseManageQuestionFragment", "Submit button clicked with questions: $questions")

            }
        }
    }

    private fun updateFileName(uri: Uri) {
        val fileName = getFileName(uri)
        binding?.etTextSelectedFileName?.text = fileName ?: "No file selected"
        binding?.etUploadCSV?.setImageResource(R.drawable.ic_selected)
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = cursor.getColumnIndex("_display_name")
            if (cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return null
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.finish.collect{
                findNavController().popBackStack()
            }
        }
    }

}