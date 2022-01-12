package com.phobez.newslite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ArticleViewModel() : ViewModel() {

    private val articles: MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    fun getArticles(): LiveData<List<Article>> {
        return articles
    }

    fun loadArticles(topic: String) {
        val url = "$ARTICLE_REQUEST_BASE_URL$topic"

        viewModelScope.launch {
            val jsonResp = QueryUtils.makeHttpRequest(url)

            if (jsonResp.isNotBlank()) {
                val res = JSONObject(jsonResp).getJSONObject(RESPONSE_KEY).getJSONArray(RESULTS_KEY)

                val maxIndex = res.length() - 1

                val newArticles = ArrayList<Article>()

                for (i in 1..maxIndex) {
                    val obj = res.getJSONObject(i)
                    val date = obj.getString(DATE_KEY)
                    val title = obj.getString(TITLE_KEY)
                    val link = obj.getString(LINK_KEY)
                    val contributors = obj.getJSONArray(TAGS_KEY)

                    val maxContributorIndex = contributors.length() - 1

                    val newContributors = ArrayList<String>()

                    for (j in 1..maxContributorIndex) {
                        val contributor = contributors.getJSONObject(j).getString(
                            TITLE_KEY
                        )

                        newContributors.add(contributor)
                    }

                    newArticles.add(Article(topic, date, title, link, newContributors))
                }

                articles.postValue(newArticles)
            }
        }
    }

    companion object {
        private const val ARTICLE_REQUEST_BASE_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&api-key=test&section="
        private const val RESPONSE_KEY = "response"
        private const val RESULTS_KEY = "results"
        private const val DATE_KEY = "webPublicationDate"
        private const val LINK_KEY = "webUrl"
        private const val TAGS_KEY = "tags"
        private const val TITLE_KEY = "webTitle"
    }
}