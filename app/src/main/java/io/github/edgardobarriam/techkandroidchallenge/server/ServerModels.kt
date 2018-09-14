package io.github.edgardobarriam.techkandroidchallenge.server

data class TagsListResult(val data: Data)

data class Data(val tags: List<Tag>)

data class Tag(val id: Int, val name: String, val display_name: String, val followers: Int, val total_items: Int, val background_hash: String)