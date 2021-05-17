package com.judge.qualimaster.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.judge.qualimaster.R
import com.judge.qualimaster.data.AppDatabase
import com.judge.qualimaster.data.AthleteDao
import com.judge.qualimaster.databinding.ActivityQualificationBinding
import com.judge.qualimaster.io.CSVReader
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@AndroidEntryPoint
class QualificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQualificationBinding
    @Inject lateinit var athleteDao: AthleteDao
    @Inject lateinit var db: AppDatabase

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launchWhenStarted {
                showProgressBar(true)
                db.clearAllTables()
                loadData(uri)
                showProgressBar(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQualificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        NavigationUI.setupWithNavController(
                binding.bnvQualificationBottomNavigation,
                findNavController(R.id.flQualification)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qualification_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.miOpenFile -> {
                getContent.launch("text/*")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun loadData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)

        if (inputStream != null) {
            val reader = CSVReader(inputStream)
            val athletes = reader.getAthletes()
            athleteDao.insertAthletes(athletes)
            Toast.makeText(this, "Successfully loaded CSV", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Problem while loading file in ${uri.path}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
        }
    }
}