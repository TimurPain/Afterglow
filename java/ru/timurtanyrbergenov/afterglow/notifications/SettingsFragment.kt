package ru.timurtanyrbergenov.afterglow.notifications

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.timurtanyrbergenov.afterglow.R

class SettingsFragment : Fragment(R.layout.fragment_notifications) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = requireContext()
            .getSharedPreferences("settings", Context.MODE_PRIVATE)

        val switch = view.findViewById<SwitchMaterial>(R.id.switchNotifications)

        switch.isChecked = prefs.getBoolean("notify_enabled", true)

        switch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean("notify_enabled", isChecked)
                .apply()
        }
    }
}
