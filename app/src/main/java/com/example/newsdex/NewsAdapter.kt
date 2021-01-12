package com.example.newsdex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class NewsAdapter(val itemClickListener: NewsItemClicked) : RecyclerView.Adapter<NewsViewHolder>() {

    var newsList: MutableList<News> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
//        This is the current View which we will bind the data with
        val viewHolder = NewsViewHolder(view)
//        This is the view Holder object which we use to get the position of the view inside the adapter.
        view.setOnClickListener{
            itemClickListener.onItemClicked(newsList[viewHolder.adapterPosition])
//            This is how we set onClickListener to the views inside Recycler view [We create an Interface and then we call the onItemClicked function present inside the Interface]
        }
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
//        This is used to get the size of the list [Number of Elements]
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNewsItem = newsList[position]
        holder.newsDate.text = currentNewsItem.date
        holder.newsTitle.text = currentNewsItem.newsTitle
        holder.newsDescription.text = currentNewsItem.newsDescription
        holder.newsSource.text = currentNewsItem.newsSource
        Glide.with(holder.itemView.context).load(currentNewsItem.imageURL).into(holder.newsImage)
//        This is how we map/bind the data inside the List to the Views
    }

    fun updateNews(updatedNews: MutableList<News>) {
        newsList.clear()
        newsList.addAll(updatedNews)
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var newsTitle = itemView.findViewById<TextView>(R.id.titleTextView)
    var newsDescription = itemView.findViewById<TextView>(R.id.descriptionTextView)
    var newsSource = itemView.findViewById<TextView>(R.id.sourceTextView)
    var newsDate = itemView.findViewById<TextView>(R.id.dateTextView)
    var newsImage = itemView.findViewById<ImageView>(R.id.newsImageView)

//    We get all the single Views inside the ViewGroup like this
}

interface NewsItemClicked {
    fun onItemClicked(currentNewsItem: News)
//    We will implement this function inside the Main Activity
}