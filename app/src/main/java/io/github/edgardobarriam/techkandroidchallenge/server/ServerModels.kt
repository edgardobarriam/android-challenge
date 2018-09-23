package io.github.edgardobarriam.techkandroidchallenge.server

import android.os.Parcel
import android.os.Parcelable

data class TagsListResponse(val data: TagsListData)

data class TagsListData(val tags: List<Tag>)

data class Tag(
        val id: Int,
        val name: String,
        val display_name: String,
        val followers: Int,
        val total_items: Int,
        val background_hash: String )


data class GallerySearchResponse(val data: TagSearch)

data class TagSearch(val name: String, val display_name: String, val items: List<Gallery>)

data class Gallery(
        val id: String,
        var type: String?,
        val title: String,
        var description: String?,
        var link: String,
        val datetime: Long,
        var views: Int,
        val ups: Int,
        val downs: Int,
        val images: List<Image>? ) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArrayList(Image))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(link)
        parcel.writeLong(datetime)
        parcel.writeInt(views)
        parcel.writeInt(ups)
        parcel.writeInt(downs)
        parcel.writeTypedList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    /** Imgur API has a different structure depending on whether the gallery has
     *  a single image or a set of images. Hence, this function "converts"
     *  multiple images galleries into single image galleries (so that all galleries
     *  have a similar structure and behavior).
     */
    fun fixGallery() {
        with(this) {
            if(images != null) {
                description = images[0].description
                link = images[0].link
                type = images[0].type
                views = images[0].views
            }
        }
    }

    companion object CREATOR : Parcelable.Creator<Gallery> {
        override fun createFromParcel(parcel: Parcel): Gallery {
            return Gallery(parcel)
        }

        override fun newArray(size: Int): Array<Gallery?> {
            return arrayOfNulls(size)
        }
    }

}

data class Image(
        val link: String,
        val type: String,
        val description: String?,
        val views: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(link)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeInt(views)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}

data class GalleryCommentsResponse(val data: List<Comment>)

data class Comment(val author: String, val comment: String)

data class UploadResponse(val data : UploadResponseData)

data class UploadResponseData(val link: String)