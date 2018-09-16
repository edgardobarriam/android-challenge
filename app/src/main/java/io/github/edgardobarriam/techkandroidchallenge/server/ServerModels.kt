package io.github.edgardobarriam.techkandroidchallenge.server

data class TagsListResponse(val data: TagsListData)

data class TagsListData(val tags: List<Tag>)

data class Tag(
        val id: Int,
        val name: String,
        val display_name: String,
        val followers: Int,
        val total_items: Int,
        val background_hash: String )


data class GallerySearchResponse(val data: List<Gallery>)

data class Gallery(
        val type: String?,
        val title: String,
        val description: String?,
        val link: String,
        val datetime: Long,
        val views: Int,
        val ups: Int,
        val downs: Int,
        val images: List<Image>? )

data class Image(val link: String, val type: String, val description: String?)

data class GalleryCommentsResponse(val data: List<Comment>)

data class Comment(val author: String, val comment: String)