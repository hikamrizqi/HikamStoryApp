package com.hikam.hikamstoryapp

import com.hikam.hikamstoryapp.response.ListStoryItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                i.toString(),
                "author + $i",
                "name $i",
                "desciption $i" ,
                0.0,
                "id $i",
                0.0
            )
            items.add(storyItem)
        }
        return items
    }
}