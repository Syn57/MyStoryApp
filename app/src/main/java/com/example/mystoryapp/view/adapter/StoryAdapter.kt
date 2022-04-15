package com.example.mystoryapp.view.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ItemRowStoryBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(accountClicked: ListStoryItem, optionsCompat: ActivityOptionsCompat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (photoUrl, createdAt, name, _, _, _, _) = listStory[position]
        holder.binding.tvName.text = name
        holder.binding.tvOneWord.text = name[0].toString().uppercase(Locale.getDefault())

        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val at = holder.itemView.context.getString(R.string.time_at)
        val outputPattern = "EEE, d MMM '$at' HH:mm"
        val inputFormatter = DateTimeFormatter.ofPattern(inputPattern, Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, Locale.ENGLISH)
        val inputDate = LocalDateTime.parse(createdAt, inputFormatter)
        val outputDate = outputFormatter.format(inputDate)

        holder.binding.tvTime.text = outputDate
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.binding.ivStory)
        holder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivDummyPhotoProfile, "ts_dummy_pp"),
                    Pair(holder.binding.tvOneWord, "ts_one_word"),
                    Pair(holder.binding.tvName, "ts_name"),
                    Pair(holder.binding.tvTime, "ts_date"),
                    Pair(holder.binding.ivStory, "ts_story_img"),
                )
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition], optionsCompat)
        }
    }

    override fun getItemCount(): Int = listStory.size
}