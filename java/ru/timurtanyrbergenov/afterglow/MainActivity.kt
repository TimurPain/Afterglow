package ru.timurtanyrbergenov.afterglow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local.AppDatabase
import ru.timurtanyrbergenov.afterglow.notifications.NotificationHelper
import java.time.LocalDate
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository.EventsRepository

class MainActivity : AppCompatActivity() {

    private lateinit var repository: EventsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navController)

        // ✅ ИНИЦИАЛИЗАЦИЯ
        val dao = AppDatabase.getDatabase(this).eventsDao()
        repository = EventsRepository(dao)

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val tomorrow = LocalDate.now().plusDays(1)
            val event = repository.getEventByDate(tomorrow)
            NotificationHelper.showTomorrowEvent(this@MainActivity, event)
        }
    }
}