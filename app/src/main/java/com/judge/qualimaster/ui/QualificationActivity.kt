package com.judge.qualimaster.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.qualimaster.R
import com.judge.qualimaster.data.AppDatabase
import com.judge.qualimaster.data.AthleteDao
// import com.judge.qualimaster.data.LocalBaseRepositoryImpl
import com.judge.qualimaster.databinding.ActivityQualificationBinding
import com.judge.qualimaster.util.Constants.COMPETITION_ID_BUNDLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class QualificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQualificationBinding
    @Inject lateinit var athleteDao: AthleteDao
    @Inject lateinit var db: AppDatabase
    @Inject lateinit var repository: Repository

    private var competitionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQualificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // repository = Repository(LocalBaseRepositoryImpl(athleteDao), Location)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        competitionId = intent.extras?.getString(COMPETITION_ID_BUNDLE)
        val argument = NavArgument.Builder().setDefaultValue(competitionId).build()
        val navController = findNavController(R.id.flQualification)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)
        graph.addArgument(COMPETITION_ID_BUNDLE, argument)
        navController.graph = graph

        NavigationUI.setupWithNavController(
                binding.bnvQualificationBottomNavigation,
                navController
        )

        binding.bnvQualificationBottomNavigation.setOnNavigationItemSelectedListener { item ->
            val fragmentId = item.itemId
            val arguments = bundleOf(Pair(COMPETITION_ID_BUNDLE, competitionId))
            navController.popBackStack()
            navController.navigate(fragmentId, arguments)
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qualification_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
//            R.id.miOpenFile -> {
//                true
//            }
            R.id.miRefresh -> {
                lifecycleScope.launchWhenStarted {
                    showProgressBar(true)
                    // delay(1000)
                    repository.refreshCompetition(competitionId ?: "")
                    showProgressBar(false)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.pbLoadingCompetition.visibility = View.VISIBLE
        } else {
            binding.pbLoadingCompetition.visibility = View.GONE
        }
    }
}