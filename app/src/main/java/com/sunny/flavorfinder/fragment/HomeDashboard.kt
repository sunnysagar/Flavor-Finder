package com.sunny.flavorfinder.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunny.flavorfinder.MainActivity
import com.sunny.flavorfinder.R
import com.sunny.flavorfinder.RequestManager
import com.sunny.flavorfinder.adapterJ.PopularRecipeAdapter
import com.sunny.flavorfinder.adapterJ.RandomRecipeAdapter
import com.sunny.flavorfinder.listenersJ.PopularRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RandomRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RecipeClickListener
import com.sunny.flavorfinder.modelJ.RandomRecipeApiResponse
import com.sunny.flavorfinder.modelJ.Recipe

class HomeDashboard : Fragment() {

    private lateinit var gsc: GoogleSignInClient
    private lateinit var userName: TextView

    lateinit var dialog: ProgressDialog
    lateinit var manager: RequestManager
    lateinit var randomRecipeAdapter: RandomRecipeAdapter

    lateinit var popularRecipeAdapter: PopularRecipeAdapter
    lateinit var recyclerViewRandom: RecyclerView
    lateinit var recyclerViewPopular: RecyclerView

    lateinit var spinner: Spinner

    var tags: MutableList<String> = mutableListOf()
    lateinit var searchView: SearchView

    lateinit var bottomNav : BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_dashboard, container, false)

        dialog = ProgressDialog(activity)
        dialog.setTitle("Loading...")

        userName = view.findViewById(R.id.userName)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct.givenName

            userName.text = "Hey " + personName

        }

        recyclerViewPopular = view.findViewById(R.id.recycler_popular)
        recyclerViewRandom = view.findViewById(R.id.recycler_random)

        searchView = view.findViewById(R.id.searchView_home)

        // Set OnClickListener on the SearchView's query text area and search icon
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                navigateToSearchResultsFragment()
            }
        }

        searchView.setOnClickListener {
            navigateToSearchResultsFragment()
        }


        tags.addAll(resources.getStringArray(R.array.tags))


        spinner = view.findViewById(R.id.spinner_tags)
        val arrayAdapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.tags,
                R.layout.spinner_text
            )
        }
        arrayAdapter?.setDropDownViewResource(R.layout.spinner_inner_text)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = spinnerSelectedListener

        manager = activity?.let { RequestManager(it) }!!

        manager.getTopPopularRecipes(popularRecipeResponeListener)
        dialog.show()

        val mainActivity = activity as MainActivity
        bottomNav = mainActivity.findViewById(R.id.bottomNav);


        return view
    }



    private val randomRecipeResponeListener: RandomRecipeResponeListener =
        object : RandomRecipeResponeListener {
            override fun didFetch(response: RandomRecipeApiResponse, message: String) {
                 recyclerViewRandom.setHasFixedSize(true)
                recyclerViewRandom.layoutManager = GridLayoutManager(activity, 1)

                randomRecipeAdapter =
                    RandomRecipeAdapter(activity, response.recipes, recipeClickListener)
                recyclerViewRandom.adapter = randomRecipeAdapter
            }

            override fun didError(message: String) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }


    private val popularRecipeResponeListener: PopularRecipeResponeListener =
        object : PopularRecipeResponeListener {
            override fun didFetch(list: ArrayList<Recipe>, message: String) {
                dialog.dismiss()

                recyclerViewPopular.setHasFixedSize(true)
                recyclerViewPopular.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

                popularRecipeAdapter =
                    PopularRecipeAdapter(activity, list, recipeClickListener)
                recyclerViewPopular.adapter = popularRecipeAdapter
            }


            override fun didError(message: String) {
            }
        }

    private val spinnerSelectedListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                tags.clear()
                tags.add(parent.selectedItem.toString())
                manager.getRandomRecipe(randomRecipeResponeListener, tags)
                //            dialog.show();
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

//    private val recipeClickListener = RecipeClickListener { id -> startActivity(
//                Intent(activity, RecipeDetailsActivity2::class.java
//                ).putExtra("id", id)
//            )
//        }

    private val recipeClickListener =
        RecipeClickListener { id ->
            val recipeDetailsFragment = RecipeDetailsFragment()

            // using bundle to pass the data
            val bundle = Bundle().apply {
                putInt("id", id.toInt())
            }

            recipeDetailsFragment.arguments = bundle

            (activity as MainActivity).loadFragment(recipeDetailsFragment)
//            val bottomNave = activity as MainActivity.bottomNav
            bottomNav.visibility = View.GONE
        }

    private fun navigateToSearchResultsFragment(){
        val searchFragment = SearchFragment()
        val mainActivity = activity as MainActivity
        mainActivity.loadFragment(searchFragment)

        bottomNav.visibility = View.GONE
    }

}