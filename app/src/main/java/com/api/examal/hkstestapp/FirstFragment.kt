package com.api.examal.hkstestapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.api.examal.hkstestapp.adapter.CustomExpandableListAdapter
import com.api.examal.hkstestapp.databinding.FragmentFirstBinding
import org.json.JSONObject
import java.io.InputStream

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var postModels = arrayListOf<PostModel>()
    private val dataMap = HashMap<String, MutableList<PostModel>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        read_json()
    }

    fun read_json() {
        var json: String? = null
        try {

            val inputStream: InputStream =
                requireContext().assets.open("sortdata.json")
            json = inputStream.bufferedReader().use { it.readText() }

            val jsonarr = JSONObject(json).getJSONArray("sort")

            for (i in 0 until jsonarr.length()) {
                val jsonobj = jsonarr.getJSONObject(i)
                postModels.add(
                    PostModel(
                        mID = jsonobj.getString("Mid"),
                        tID = jsonobj.getString("Tid"),
                        amount = jsonobj.getString("amount"),
                        narration = jsonobj.getString("narration"),
                    )
                )
            }

            // Check if mID not exist then add its data in map or if exist then get the list and update.
            postModels.forEach {
                if (!dataMap.containsKey(it.mID)) {
                    dataMap[it.mID] = mutableListOf(it)
                } else {
                    dataMap[it.mID]?.add(it)
                }
            }

            val expandableListAdapter = CustomExpandableListAdapter(
                requireContext(),
                postModels.toSet().toList().distinctBy { it.mID },  // Unique item list based on mID
                dataMap
            )
            binding.jsonList.setAdapter(expandableListAdapter)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}