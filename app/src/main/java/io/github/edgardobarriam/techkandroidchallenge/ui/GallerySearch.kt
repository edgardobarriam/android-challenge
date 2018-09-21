package io.github.edgardobarriam.techkandroidchallenge.ui

import io.github.edgardobarriam.techkandroidchallenge.server.Gallery

class GallerySearch {

    fun search(list: List<Gallery>, title: String = "", description: String = "") : List<Gallery> {


        if (title.isNotBlank() && description.isBlank()) {
            return list.filter { it.title.contains(title, true) }
        }

        // Since we use the gallery description from this point, need to filter not null descriptions
        val notNullDescriptionList = list.filter { it.description != null }

        if(title.isNotBlank() && description.isNotBlank()) {
            return notNullDescriptionList.filter {
                it.title.contains(title,true)
                        && it.description!!.contains(description,true)
            }

        } else if(title.isBlank() && description.isNotBlank()) {
            return notNullDescriptionList.filter { it.description!!.contains(description, true)}

        }

        return list
    }
}