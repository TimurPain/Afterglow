package ru.timurtanyrbergenov.afterglow.fragments.importantdates.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.R
import ru.timurtanyrbergenov.afterglow.databinding.FragmentImportantDatesBinding
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import java.time.LocalDate

class ImportantDatesFragment : Fragment(R.layout.fragment_important_dates) {

    private var _binding: FragmentImportantDatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventsAdapter: EventsAdapter
    private var selectedDate: LocalDate = LocalDate.now()

    private val viewModel: ImportantDatesViewModel by viewModels {
        ImportantDatesViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImportantDatesBinding.bind(view)

        setupRecycler()
        setupCalendar()
        setupFab()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // --------------------
    // UI
    // --------------------

    private fun setupRecycler() {
        eventsAdapter = EventsAdapter { event ->
            openEditBottomSheet(event)
        }

        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventsAdapter
        }

        setupSwipeToDelete()
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDate = LocalDate.of(year, month + 1, day)
            viewModel.selectDate(selectedDate)
        }
    }

    private fun setupFab() {
        binding.fabAddEvent.setOnClickListener {
            AddEventBottomSheet(
                selectedDate = selectedDate,
                onSave = { event ->
                    viewModel.addEvent(event)
                }
            ).show(parentFragmentManager, "AddEvent")
        }
    }

    // --------------------
    // Data
    // --------------------

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { events ->
                    eventsAdapter.submitList(events)

                    binding.textEmpty.visibility =
                        if (events.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    // --------------------
    // Actions
    // --------------------

    private fun openEditBottomSheet(event: EventEntity) {
        AddEventBottomSheet(
            selectedDate = event.date,
            existingEvent = event,
            onSave = { updated ->
                viewModel.updateEvent(updated)
            }
        ).show(parentFragmentManager, "EditEvent")
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val event = eventsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteEvent(event)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerEvents)
    }
}
