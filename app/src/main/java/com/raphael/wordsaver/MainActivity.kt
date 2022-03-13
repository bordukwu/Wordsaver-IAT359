package com.raphael.wordsaver

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchUIUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var Addword: FloatingActionButton
    private lateinit var wordDao: WordDao
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: RVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        Addword = findViewById(R.id.Addword)

        wordDao= WordDao()
        auth = Firebase.auth

        Addword.setOnClickListener {
            // Navigates to the add word activity
            val intent = Intent(this,AddwordActivity::class.java
            )
            startActivity(intent)

        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        // gets the word collection from the worddao class and the current user id using auth
        val wordCollection = wordDao.wordCollection
        val currentUserId = auth.currentUser!!.uid

            // creates a query in word collection. The query class is used for reading data in firebase
        //the whereEqualTO if so that the user has access only to his own WORDS and the order by shows the words in alphabetical order
        val query = wordCollection.whereEqualTo("uid",currentUserId).orderBy("text",Query.Direction.ASCENDING)


        // create a firestore Recycler view option
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Word>().setQuery(query,Word::class.java).build()

        adapter = RVAdapter(recyclerViewOption)
        recyclerView.adapter = adapter

        // swipe to delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position = viewHolder.absoluteAdapterPosition
                adapter.deleteNote(position)
            }
        }).attachToRecyclerView(recyclerView)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}