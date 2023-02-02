package com.example.newsapp1

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // Toast.makeText(this, "Not Loading", Toast.LENGTH_LONG).show()
        // common line for both adapters
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set the LayoutManager that this RecyclerView will use.
        recyclerView.layoutManager = LinearLayoutManager(this)
        //

        /**
        // Adapter class is initialized and list is passed in the param.
        val items2 = getItemsList()
        val itemAdapter = ItemAdapter(this, items2)

        // adapter instance is set to the recyclerview to inflate the items.
        recycler_view_items.adapter = itemAdapter
        //
         */

        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

        // val listView: ListView = findViewById<ListView>(R.id.list) as ListView

    }

    private fun fetchData() {
        val url = "https://newsdata.io/api/1/news?apikey=pub_9718823be7afb469b3231fa9f2d43160eb73"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("source_id"),
                        newsJsonObject.getString("link"),
                        newsJsonObject.getString("image_url")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Not Loading", Toast.LENGTH_LONG).show()
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    //
    /**
     * Function is used to get the Items List which is added in the list.
     */
//    private fun getItemsList(): ArrayList<String> {
//        val list = ArrayList<String>()
//
//        for(i in 1..20){
//            list.add("Item is $i")
//        }
//        return list
//    }
    //
    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.link))
    }


}

