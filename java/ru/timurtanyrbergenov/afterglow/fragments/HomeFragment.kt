package ru.timurtanyrbergenov.afterglow.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.databinding.FragmentHomeBinding
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.AppDatabase as DailyTasksDatabase
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.repository.DailyTasksRepository
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.DailyListItem
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.DailyTasksViewModel
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.DailyTasksViewModelFactory
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui.TodayTaskMiniAdapter
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local.AppDatabase
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventType
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository.EventsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventsViewModel: HomeViewModel
    private lateinit var dailyTasksViewModel: DailyTasksViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // ViewModel для событий
        val eventsDao = AppDatabase.getDatabase(requireContext()).eventsDao()
        val eventsRepository = EventsRepository(eventsDao)
        eventsViewModel = ViewModelProvider(this, HomeViewModelFactory(eventsRepository))
            .get(HomeViewModel::class.java)

        // ViewModel для ежедневных задач
        val dailyTasksDao = DailyTasksDatabase.get(requireContext()).dailyTaskDao()
        val dailyTasksRepository = DailyTasksRepository(dailyTasksDao)
        dailyTasksViewModel = ViewModelProvider(this, DailyTasksViewModelFactory(dailyTasksRepository))
            .get(DailyTasksViewModel::class.java)

        observeNearestEvent()
        observeTodayProgressAndTasks()

        // Генерация задач на сегодня
        lifecycleScope.launch {
            dailyTasksViewModel.getTodayTasks()
        }
    }

    private fun observeNearestEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventsViewModel.nearestEvent.collect { event ->
                    if (event != null) {
                        binding.cardNearestEvent.visibility = View.VISIBLE
                        binding.textEventTitle.text = event.title
                        binding.textEventDate.text = formatNearestDate(event.date)
                        binding.textEventType.text = event.type.title

                        val colorRes = when (event.type) {
                            EventType.BIRTHDAY -> R.color.red
                            EventType.ANNIVERSARY -> R.color.orange
                            EventType.FIRST_MEET -> R.color.green
                            EventType.GIFT -> R.color.yellow
                            else -> R.color.grey
                        }
                        binding.indicatorColor.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
                    } else {
                        binding.cardNearestEvent.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeTodayProgressAndTasks() {
        // Прогресс + статус (как раньше)
        dailyTasksViewModel.totalProgress.observe(viewLifecycleOwner) { progress ->
            ObjectAnimator.ofInt(binding.mainGradientProgressBar, "progress",
                binding.mainGradientProgressBar.progress, progress.percent).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            binding.mainBoyStatusText.text = when {
                progress.percent >= 90 -> "Статус: идеальный парень"
                progress.percent >= 70 -> "Статус: отличный парень"
                progress.percent >= 50 -> "Статус: хороший парень"
                progress.percent >= 30 -> "Статус: нормальный парень"
                else -> "Статус: надо стараться"
            }
        }

        // Интерактивный список мини-задач
        dailyTasksViewModel.items.observe(viewLifecycleOwner) { items ->
            val todayTasks = items.filterIsInstance<DailyListItem.TodayTask>()
                .map { it.task }

            if (todayTasks.isEmpty()) {
                // Можно показать заглушку, если задач нет
                binding.todayTasksRecycler.visibility = View.GONE
            } else {
                binding.todayTasksRecycler.visibility = View.VISIBLE
                binding.todayTasksRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.todayTasksRecycler.adapter = TodayTaskMiniAdapter(todayTasks) { task, action ->
                    when (action) {
                        TodayTaskMiniAdapter.Action.COMPLETE -> dailyTasksViewModel.complete(task)
                        TodayTaskMiniAdapter.Action.SKIP -> dailyTasksViewModel.skip(task)
                    }
                }
            }
        }
    }
    private fun formatNearestDate(date: LocalDate): String {
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(today, date)
        return when (daysBetween) {
            0L -> "Сегодня"
            1L -> "Завтра"
            in 2..7 -> "Через $daysBetween ${pluralDays(daysBetween)}"
            else -> date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru")))
        }
    }

    private fun pluralDays(days: Long): String {
        return when {
            days % 10 == 1L && days % 100 != 11L -> "день"
            days % 10 in 2..4 && days % 100 !in 12..14 -> "дня"
            else -> "дней"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}