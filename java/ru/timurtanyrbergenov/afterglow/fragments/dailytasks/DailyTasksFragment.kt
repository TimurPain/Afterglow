package ru.timurtanyrbergenov.afterglow.fragments.dailytasks

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.AppDatabase
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.repository.DailyTasksRepository
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.Action
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.DailyTasksAdapter
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.DailyTasksViewModel
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.DailyTasksViewModelFactory

class DailyTasksFragment : Fragment(R.layout.fragment_daily_tasks) {

    private val viewModel: DailyTasksViewModel by lazy {
        val database = AppDatabase.get(requireContext())
        val dao = database.dailyTaskDao()
        val repository = DailyTasksRepository(dao)
        ViewModelProvider(this, DailyTasksViewModelFactory(repository))
            .get(DailyTasksViewModel::class.java)
    }

    private lateinit var adapter: DailyTasksAdapter
    private lateinit var gradientProgressBar: ProgressBar
    private lateinit var boyStatusText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация view
        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        gradientProgressBar = view.findViewById(R.id.gradientProgressBar)
        boyStatusText = view.findViewById(R.id.boyStatusText)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DailyTasksAdapter { task, action ->
            when (action) {
                Action.COMPLETE -> viewModel.complete(task)
                Action.SKIP -> viewModel.skip(task)
            }
        }

        recyclerView.adapter = adapter

        // Наблюдение за списком задач
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        viewModel.totalProgress.observe(viewLifecycleOwner) { progress ->
            // Анимация прогресс-бара
            ObjectAnimator.ofInt(gradientProgressBar, "progress", gradientProgressBar.progress, progress.percent).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            // Новый статус
            val (status, textColor) = when {
                progress.percent >= 90 -> "Статус: идеальный парень" to 0xFF8BC34A.toInt()      // зелёный #8BC34A
                progress.percent >= 70 -> "Статус: отличный парень"  to 0xFF8BC34A.toInt()      // зелёный
                progress.percent >= 50 -> "Статус: хороший парень"   to 0xFFFF9800.toInt()      // оранжевый #FF9800
                progress.percent >= 30 -> "Статус: нормальный парень" to 0xFFB0B0B0.toInt()     // grey #B0B0B0
                else -> "Статус: надо стараться" to 0xFFC62828.toInt()                   // красный #C62828
            }

            boyStatusText.text = status
            boyStatusText.setTextColor(textColor)
        }

        // Одноразовая генерация задач на сегодня
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTodayTasks()  // Метод, который мы добавили в ViewModel
        }
    }
}