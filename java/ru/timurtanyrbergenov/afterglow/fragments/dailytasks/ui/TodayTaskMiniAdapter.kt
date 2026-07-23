package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.LoveLanguage
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskTemplate

class TodayTaskMiniAdapter(
    private val tasks: List<TaskTemplate>,
    private val onAction: (TaskTemplate, Action) -> Unit
) : RecyclerView.Adapter<TodayTaskMiniAdapter.MiniTaskVH>() {

    enum class Action { COMPLETE, SKIP }

    class MiniTaskVH(view: View) : RecyclerView.ViewHolder(view) {
        val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val completeButton: ImageButton = view.findViewById(R.id.completeButton)
        val skipButton: ImageButton = view.findViewById(R.id.skipButton)

        fun bind(task: TaskTemplate, onAction: (TaskTemplate, Action) -> Unit) {
            titleText.text = task.title

            // Иконка категории
            categoryIcon.setImageResource(when (task.loveLanguage) {
                LoveLanguage.WORDS -> R.drawable.ic_words
                LoveLanguage.ACTS -> R.drawable.ic_acts
                LoveLanguage.TIME -> R.drawable.ic_time
                LoveLanguage.GIFTS -> R.drawable.ic_gifts
                LoveLanguage.TOUCH -> R.drawable.ic_touch
            })

            completeButton.setOnClickListener { onAction(task, Action.COMPLETE) }
            skipButton.setOnClickListener { onAction(task, Action.SKIP) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniTaskVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_today_task_mini, parent, false)
        return MiniTaskVH(view)
    }

    override fun onBindViewHolder(holder: MiniTaskVH, position: Int) {
        holder.bind(tasks[position]) { task, action ->
            onAction(task, action)
        }
    }

    override fun getItemCount(): Int = tasks.size
}