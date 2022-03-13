package com.raphael.wordsaver

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

// Recycler view adapter

class RVAdapter(options: FirestoreRecyclerOptions<Word>) :FirestoreRecyclerAdapter<Word,RVAdapter.RVViewHolder>(
    options
) {
    class RVViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview){
        val wordText:TextView = itemview.findViewById(R.id.wordText)
    }
    fun deleteNote(position: Int)
    {
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        return RVViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int, model: Word) {
        holder.wordText.text = model.text
    }

}