 package com.example.newsdex

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

 class MainActivity : AppCompatActivity(), NewsItemClicked {

     var newsList = mutableListOf<News>()
     private lateinit var adapter: NewsAdapter
     lateinit var newsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        adapter = NewsAdapter(this)
        newsRecyclerView.adapter = adapter
    }

     fun fetchData() : MutableList<News>{
//         Fetch the Data using volley library and add it to a List and return it.
         val url: String = ""
//         newsapi.org is replaced by media stack api because News API didn't support api calls other than localhost.
//         media stack api also doesn't support  https and hence, I had to set up network_security_config.xml to allow http call
         val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                 Response.Listener { response ->
                     val articlesArray = response.getJSONArray("data")
                     for(i in 0..articlesArray.length()-1)
                     {
                         var currentArticle: JSONObject = articlesArray[i] as JSONObject
                         val imageURL: String =  currentArticle.getString("image")
                         val newsTitle: String = currentArticle.getString("title")
                         val newsDescription: String = currentArticle.getString("description")
                         val newsSource: String = currentArticle.getString("source")
                         var publishedDate: String = currentArticle.getString("published_at")
                         publishedDate = publishedDate.subSequence(0,10) as String
                         val articleURL: String = currentArticle.getString("url")
                         newsList.add(News(imageURL, newsTitle, newsDescription, newsSource, publishedDate, articleURL))
                     }
                     adapter.updateNews(newsList)
                 },
                 Response.ErrorListener { error ->
                     // TODO: Handle
                     Log.e("Fetch Data:", "Some error has occurred in parsing the JSON")
                     Log.e("Fetch Data:", error.toString())
                 }
         )

        // Access the RequestQueue through your singleton class.
         MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
         return newsList
     }

     override fun onItemClicked(currentNewsItem: News) {
//         Logic to open a new custom Tab
         val url = currentNewsItem.newsArticleURL
         val builder = CustomTabsIntent.Builder()
         val customTabsIntent = builder.build()
         customTabsIntent.launchUrl(this, Uri.parse(url))
     }
 }
