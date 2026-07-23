package ru.timurtanyrbergenov.afterglow.fragments.importantdates.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.timurtanyrbergenov.afterglow.databinding.ItemEventBinding
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity

class EventsAdapter(
    private val onClick: (EventEntity) -> Unit
) : ListAdapter<EventEntity, EventsAdapter.EventViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val onClick: (EventEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.textTitle.text = event.title
            binding.textDescription.text = event.description
            binding.textType.text = event.type.title

            // Цвет индикатора (у тебя уже реальный цвет, не @ColorRes — это правильно)
            binding.viewIndicator.setBackgroundColor(event.type.color)

            // 🔥 ВОТ ГЛАВНОЕ — ВОЗВРАЩАЕМ РЕДАКТИРОВАНИЕ ПО НАЖАТИЮ
            binding.root.setOnClickListener {
                onClick(event)
            }
        }
    }

    override fun submitList(list: List<EventEntity>?) {
        // сортировка: дата ↑, приоритет ↓
        val sorted = list?.sortedWith(
            compareBy<EventEntity> { it.date }
                .thenByDescending { it.type.priority }
        )
        super.submitList(sorted)
    }

    class DiffCallback : DiffUtil.ItemCallback<EventEntity>() {
        override fun areItemsTheSame(old: EventEntity, new: EventEntity): Boolean =
            old.id == new.id

        override fun areContentsTheSame(old: EventEntity, new: EventEntity): Boolean =
            old == new
    }
}
