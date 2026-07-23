package ru.timurtanyrbergenov.afterglow.fragments.importantdates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.timurtanyrbergenov.afterglow.databinding.BottomSheetAddEventBinding
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventType
import java.time.LocalDate

class AddEventBottomSheet(
    private val selectedDate: LocalDate,
    private val existingEvent: EventEntity? = null,
    private val onSave: (EventEntity) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventTypes: Array<EventType>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        existingEvent?.let { event ->
            binding.editTitle.setText(event.title)
            binding.editDescription.setText(event.description)
            binding.spinnerType.setSelection(event.type.ordinal)
        }

        setupSaveButton()
    }

    private fun setupSpinner() {
        eventTypes = EventType.values()

        binding.spinnerType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            eventTypes.map { it.title }
        )
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.editTitle.text.toString().trim()
            if (title.isEmpty()) return@setOnClickListener

            val description = binding.editDescription.text.toString().trim()
            val type = eventTypes[binding.spinnerType.selectedItemPosition]

            // Проверка: если создаём новое событие и дата в прошлом — запрещаем
            if (existingEvent == null && selectedDate.isBefore(LocalDate.now())) {
                binding.editTitle.error = "Нельзя добавить событие в прошлое"
                return@setOnClickListener
            }

            val event = existingEvent?.copy(
                title = title,
                description = description,
                type = type,
                priority = type.color
            ) ?: EventEntity(
                title = title,
                description = description,
                date = selectedDate,
                type = type,
                priority = type.color
            )

            onSave(event)
            dismiss()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
