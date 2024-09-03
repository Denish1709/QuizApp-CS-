package com.denish.quizappcs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.denish.quizappcs.data.model.quiz.Quiz
import com.denish.quizappcs.databinding.ItemQuizBinding
import com.denish.quizappcs.ui.teacher.detail.DetailFragment
import com.google.android.material.animation.AnimatableView.Listener

class QuizAdapter(
    private var quizes: List<Quiz>
): RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    var listener : Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val item = quizes[position]
        holder.bind(item)
    }

    fun setQuizes(quizes: List<Quiz>) {
        this.quizes = quizes
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: ItemQuizBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.tvTitle.text = quiz.title
            binding.tvDesc.text = quiz.desc
            binding.ivEdit.setOnClickListener {
                listener?.onEditItem(quiz)
            }
            binding.ivDelete.setOnClickListener{
                listener?.onDeleteItem(quiz)
            }
            binding.cvQuizItem.setOnClickListener{
                listener?.onClickItem(quiz)
            }
        }
    }

    interface Listener {

        fun onEditItem(quiz: Quiz)
        fun onClickItem(quiz: Quiz)
        fun onDeleteItem(quiz: Quiz)
    }

}