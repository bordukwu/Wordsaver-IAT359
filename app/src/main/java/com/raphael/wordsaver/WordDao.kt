package com.raphael.wordsaver

import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class WordDao {
    private val db = FirebaseFirestore.getInstance()
    // creates collection for the notes
    val wordCollection = db.collection("Words")
    private val auth  = Firebase.auth

    fun addWord(text: String)
    {
        val currentUserId = auth.currentUser!!.uid
        val word = Word(text,currentUserId)
        wordCollection.document().set(word)
    }






}