package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.DailyListItem
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.LoveLanguage
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskStatus
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskTemplate

enum class Action { COMPLETE, SKIP }

class DailyTasksAdapter(
    private val onTodayClick: (TaskTemplate, Action) -> Unit
) : ListAdapter<DailyListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_TODAY_HEADER = 0
        private const val TYPE_TODAY_TASK = 1
        private const val TYPE_HISTORY_HEADER = 2
        private const val TYPE_DAY_HEADER = 3
        private const val TYPE_HISTORY_TASK = 4

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyListItem>() {
            override fun areItemsTheSame(old: DailyListItem, new: DailyListItem): Boolean =
                when {
                    old is DailyListItem.TodayTask && new is DailyListItem.TodayTask -> old.task.id == new.task.id
                    old is DailyListItem.HistoryTask && new is DailyListItem.HistoryTask -> old.task.id == new.task.id
                    else -> old == new
                }

            override fun areContentsTheSame(old: DailyListItem, new: DailyListItem): Boolean =
                old == new
        }
    }

    // ViewHolders
    class HeaderVH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.headerTitle)
    }

    class TodayTaskVH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.titleText)
        private val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)
        private val complete: ImageButton = view.findViewById(R.id.completeButton)
        private val skip: ImageButton = view.findViewById(R.id.skipButton)

        fun bind(task: TaskTemplate, onClick: (TaskTemplate, Action) -> Unit) {
            title.text = task.title

            // Иконка категории
            categoryIcon.setImageResource(when (task.loveLanguage) {
                LoveLanguage.WORDS -> R.drawable.ic_words
                LoveLanguage.ACTS -> R.drawable.ic_acts
                LoveLanguage.TIME -> R.drawable.ic_time
                LoveLanguage.GIFTS -> R.drawable.ic_gifts
                LoveLanguage.TOUCH -> R.drawable.ic_touch
            })

            complete.setOnClickListener { onClick(task, Action.COMPLETE) }
            skip.setOnClickListener { onClick(task, Action.SKIP) }
        }
    }

    class DayVH(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.dateText)
        fun bind(date: String) { dateText.text = date }
    }

    class HistoryTaskVH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.titleText)
        private val categoryIcon: ImageView = view.findViewById(R.id.categoryIcon)

        fun bind(task: TaskTemplate) {
            title.text = task.title

            // Иконка категории
            categoryIcon.setImageResource(when (task.loveLanguage) {
                LoveLanguage.WORDS -> R.drawable.ic_words2
                LoveLanguage.ACTS  -> R.drawable.ic_acts
                LoveLanguage.TIME  -> R.drawable.ic_time2
                LoveLanguage.GIFTS -> R.drawable.ic_gifts2
                LoveLanguage.TOUCH -> R.drawable.ic_touch2
            })

            // Опционально: сделать текст серым для пропущенных задач
            title.setTextColor(when (task.status) {
                TaskStatus.SKIPPED -> 0xFF757575.toInt() // серый
                else -> 0xFF000000.toInt() // чёрный
            })
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DailyListItem.TodayHeader -> TYPE_TODAY_HEADER
        is DailyListItem.TodayTask -> TYPE_TODAY_TASK
        is DailyListItem.HistoryHeader -> TYPE_HISTORY_HEADER
        is DailyListItem.DayHeader -> TYPE_DAY_HEADER
        is DailyListItem.HistoryTask -> TYPE_HISTORY_TASK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TODAY_HEADER -> HeaderVH(inflater.inflate(R.layout.item_header_today, parent, false))
            TYPE_TODAY_TASK -> TodayTaskVH(inflater.inflate(R.layout.item_task_today, parent, false))
            TYPE_HISTORY_HEADER -> HeaderVH(inflater.inflate(R.layout.item_header_history, parent, false))
            TYPE_DAY_HEADER -> DayVH(inflater.inflate(R.layout.item_header_day, parent, false))
            TYPE_HISTORY_TASK -> HistoryTaskVH(inflater.inflate(R.layout.item_task_history, parent, false))
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DailyListItem.TodayTask -> (holder as TodayTaskVH).bind(item.task, onTodayClick)
            is DailyListItem.HistoryTask -> (holder as HistoryTaskVH).bind(item.task)
            is DailyListItem.DayHeader -> (holder as DayVH).bind(item.date)
            else -> Unit
        }
    }
}