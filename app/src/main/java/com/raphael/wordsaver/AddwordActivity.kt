package com.raphael.wordsaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddwordActivity : AppCompatActivity() {

    private lateinit var wordEditText: EditText
    private lateinit var saveButton: FloatingActionButton
    private lateinit var wordDao: WordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addword)

        wordEditText = findViewById(R.id.WordEditText)
        saveButton = findViewById(R.id.saveButton)
        wordDao = WordDao()

        saveButton.setOnClickListener {
            val note = wordEditText.text.toString()
            if (note.isNotEmpty())
            {
                //navigate back to main activity after save
                    wordDao.addWord(note)
                val intent =Intent(this,MainActivity::class.java )
                startActivity(intent)

            }
            else{
                val intent =Intent(this,MainActivity::class.java )
                startActivity(intent)
            }
        }
    }


}