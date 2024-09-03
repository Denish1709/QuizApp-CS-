package com.denish.quizappcs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.denish.quizappcs.data.model.result.Result
import com.denish.quizappcs.databinding.ItemStudentMarkBinding

class StudentMarksAdapter: ListAdapter<Result, StudentMarksAdapter.ViewHolder>(StudentMarkDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = getItem(position)
        holder.bind(mark)
    }

    class ViewHolder(private val binding: ItemStudentMarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mark: Result) {
            binding.tvStudentName.text = mark.studentName
            binding.tvMarks.text = mark.mark.toString()
        }
    }

    class StudentMarkDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.studentName == newItem.studentName
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}