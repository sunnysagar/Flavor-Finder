package com.sunny.flavorfinder.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sunny.flavorfinder.MainActivity
import com.sunny.flavorfinder.R
import com.sunny.flavorfinder.RequestManager
import com.sunny.flavorfinder.adapterJ.RandomRecipeAdapter
import com.sunny.flavorfinder.adapterJ.SearchAdapter
import com.sunny.flavorfinder.listenersJ.RandomRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RecipeClickListener
import com.sunny.flavorfinder.listenersJ.SearchedRecipeResponseListener
import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse
import com.sunny.flavorfinder.modelJ.Result
import com.sunny.flavorfinder.modelJ.Root
import com.sunny.flavorfinder.modelJ.SearchRecipeApiResponse

class SearchFragment : Fragment() {

//    val context = activity?.applicationContex
    //    t

    lateinit var dialog: ProgressDialog
    lateinit var btnn :Button
    lateinit var searchView_search : SearchView

    lateinit var recyclerViewSearched: RecyclerView
    lateinit var searchedRecipeAdapter: SearchAdapter

    lateinit var bottomNav : BottomNavigationView
    var tags: MutableList<String> = mutableListOf()

    lateinit var manager:RequestManager


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        btnn = view.findViewById(R.id.btnBottom)
        searchView_search = view.findViewById(R.id.searchView_search)
        recyclerViewSearched = view.findViewById(R.id.recycler_random_search)

        recyclerViewSearched.setHasFixedSize(true)

        dialog = ProgressDialog(activity)
        dialog.setTitle("Loading...")


        tags.addAll(resources.getStringArray(R.array.tags))

//        manager = activity?.let { RequestManager(it) }!!
        manager = RequestManager(requireActivity())

        searchView_search.setOnClickListener {
            val homeDashboard = HomeDashboard()
            val mainActivity = activity as MainActivity
            mainActivity.loadFragment(homeDashboard)

            bottomNav.visibility = View .VISIBLE
        }

        searchView_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                manager.getSearchedRecipe(searchRecipeResponseListener,query)
                 dialog.show();
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        val mainActivity = activity as MainActivity
        bottomNav = mainActivity.findViewById(R.id.bottomNav);


        return view
    }

    private val searchRecipeResponseListener: SearchedRecipeResponseListener =
        object : SearchedRecipeResponseListener {
            override fun didFetch(response: Root?, message: String?) {
                dialog.dismiss()
                recyclerViewSearched.setHasFixedSize(true)
                recyclerViewSearched.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

                searchedRecipeAdapter = SearchAdapter(activity, response!!.results, recipeClickListener)

                recyclerViewSearched.adapter = searchedRecipeAdapter
            }

            override fun didError(message: String?) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

    private val recipeClickListener =
        RecipeClickListener { id ->
            val bottomRecipeDetailsFragment = BottomRecipeDetailsFragment.newInstance(id.toInt())
            bottomRecipeDetailsFragment.show(parentFragmentManager, "BottomRecipeDetailsFragment")

//            bottomNav.visibility = View.GONE
        }


}