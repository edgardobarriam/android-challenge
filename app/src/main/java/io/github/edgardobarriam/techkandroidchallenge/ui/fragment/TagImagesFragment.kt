package io.github.edgardobarriam.techkandroidchallenge.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_tag_images.*

/**
 * A fragment representing a single Tag detail screen.
 * This fragment is either contained in a [TagListActivity]
 * in two-pane mode (on tablets) or a [TagDetailActivity]
 * on handsets.
 */
class TagImagesFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_TAG_DISPLAY_NAME)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DummyContent.ITEM_MAP[it.getString(ARG_TAG_DISPLAY_NAME)]
                activity?.toolbar_layout?.title = item?.content
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tag_images_list, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            //rootView.fragment_tag_images_list.text = it.details
            // TODO: Crear adaptador para lista de imagenes del tag
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_TAG_DISPLAY_NAME = "tag_display_name"
    }
}
