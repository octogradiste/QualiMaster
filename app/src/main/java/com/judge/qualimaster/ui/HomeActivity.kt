package com.judge.qualimaster.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.judge.core.data.Repository
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.io.QualificationFileParser
import com.judge.qualimaster.R
import com.judge.qualimaster.adapter.CompetitionsRecyclerviewAdapter
// import com.judge.qualimaster.data.FirestoreBaseRepository
import com.judge.qualimaster.databinding.ActivityHomeBinding
import com.judge.qualimaster.io.LineReaderImpl
import com.judge.qualimaster.ui.viewmodels.HomeViewModel
import com.judge.qualimaster.util.Constants.COMPETITION_ID_BUNDLE
import com.judge.qualimaster.util.TimeFormatterImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            display()
        }
    }

    private suspend fun display() {
        showProgressBar(true)

        when (val comps = viewModel.getAllCompetitions()) {
            is Result.Error -> Toast.makeText(this@HomeActivity, comps.msg, Toast.LENGTH_LONG)
                .show()
            is Result.Success -> {
                val adapter = CompetitionsRecyclerviewAdapter(comps.value) { competitionId ->
                    val bundle = Bundle()
                    bundle.putString(COMPETITION_ID_BUNDLE, competitionId)
                    val intent = Intent(this@HomeActivity, QualificationActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

                binding.rvCompetitions.let {
                    it.adapter = adapter
                    it.layoutManager = LinearLayoutManager(this@HomeActivity)
                }
            }
        }

        showProgressBar(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.miOpenFile -> {
                getContent.launch("text/*")
                true
            }
            R.id.miRefresh -> {
                lifecycleScope.launchWhenStarted {
                    display()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launchWhenStarted {
                showProgressBar(true)
                loadData(uri)
                showProgressBar(false)
            }
        }
    }

    private suspend fun loadData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)

        if (inputStream != null) {
            val parser = QualificationFileParser()
            val lineReader = LineReaderImpl(inputStream)
            val timeFormatter = TimeFormatterImpl("yyyy-MM-dd HH:mm:ss")
            when (val result = parser.parse(lineReader, timeFormatter)) {
                is Result.Success -> {
                    val competition = result.value.first
                    val athletes = result.value.second
                    when (val response = viewModel.uploadNewCompetition(competition, athletes)) {
                        is Response.Success -> Toast.makeText(this, "Successfully loaded CSV", Toast.LENGTH_SHORT).show()
                        is Response.Error -> Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Problem while reading file in ${uri.path}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.pbLoadingCompetitions.visibility = View.VISIBLE
        } else {
            binding.pbLoadingCompetitions.visibility = View.GONE
        }
    }
}