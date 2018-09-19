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
        val views: Int,
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

    companion object CREATOR : Parcelable.Creator<Gallery> {
        override fun createFromParcel(parcel: Parcel): Gallery {
            return Gallery(parcel)
        }

        override fun newArray(size: Int): Array<Gallery?> {
            return arrayOfNulls(size)
        }
    }

}

data class Image(val link: String, val type: String, val description: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(link)
        parcel.writeString(type)
        parcel.writeString(description)
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