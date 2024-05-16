package com.sunny.flavorfinder.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import com.sunny.flavorfinder.MainActivity
import com.sunny.flavorfinder.R
import com.sunny.flavorfinder.RequestManager
import com.sunny.flavorfinder.adapterJ.EquipmentsAdapter
import com.sunny.flavorfinder.adapterJ.IngredientsAdapter
import com.sunny.flavorfinder.adapterJ.PopularRecipeAdapter
import com.sunny.flavorfinder.adapterJ.SimilarRecipeAdapter
import com.sunny.flavorfinder.listenersJ.EquipmentDetailsListener
import com.sunny.flavorfinder.listenersJ.PopularRecipeResponeListener
import com.sunny.flavorfinder.listenersJ.RecipeClickListener
import com.sunny.flavorfinder.listenersJ.RecipeDetailsListener
import com.sunny.flavorfinder.listenersJ.SimilarRecipeResponseListener
import com.sunny.flavorfinder.modelJ.Recipe
import com.sunny.flavorfinder.modelJ.RecipeDetailsResponse
import com.sunny.flavorfinder.modelJ.SimilarRecipeResponse
import com.sunny.flavorfinder.modelJ.Step


class BottomRecipeDetailsFragment : BottomSheetDialogFragment() {

//    lateinit var manager:RequestManager
    // heading
    lateinit var recipeTitle:TextView
    lateinit var backToHome:ImageView
    lateinit var fabRecipe:ImageView

    //Image Layout
    lateinit var imageLL: LinearLayout
    lateinit var recipeImage:ImageView
    lateinit var recipeMealTime:TextView
    lateinit var recipeMealServing:TextView
    lateinit var recipeMealPrice:TextView

    lateinit var btnIngredients:Button

    // Ingredients Layout
    lateinit var ingredientsLL: LinearLayout
    lateinit var IngredientsDropdown:ImageView
    lateinit var recipeRecyclerMealIngredients:RecyclerView
    lateinit var btnFullDetail:Button

    // Full Recipe
    lateinit var detail_n_Similar:LinearLayout
    lateinit var recipeDropdown:ImageView
    lateinit var fullRecipeRL:RelativeLayout
    lateinit var fullRecipeSV:ScrollView

    lateinit var recipeRecyclerMealEquipment:RecyclerView
    lateinit var RecipeMealSummary:TextView


    lateinit var recipeMealNutrition: TextView
    lateinit var recipeMealBadNutrition: TextView
    lateinit var recipeMealGoodNutrition: TextView

    lateinit var nutriDropdown1:ImageView
    lateinit var nutriDropup1:ImageView
    lateinit var nutriBadDropdown1:ImageView
    lateinit var nutriBadDropup1:ImageView
    lateinit var nutriGoodDropdown1:ImageView
    lateinit var nutriGoodDropup1:ImageView

    //Similar Recipe
    lateinit var similarRecipe:LinearLayout
    lateinit var btnSimilarRecipe:Button
    lateinit var similarRecipeRecycler:RecyclerView
    lateinit var similarRecipeDropdown:ImageView

    //Adapter
    lateinit var recipeIngredientsAdapter: IngredientsAdapter

    lateinit var recipeEquipmentsAdapter: EquipmentsAdapter

    lateinit var similarRecipeAdapter: SimilarRecipeAdapter




    companion object {
        private const val ARG_ID = "id"

        fun newInstance(id: Int): BottomRecipeDetailsFragment {
            val fragment = BottomRecipeDetailsFragment()
            val args = Bundle()
            args.putInt(ARG_ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme) // Applying custom style here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_details, container, false)

        initializeViews(view)
        setupClickListeners()

        val id = arguments?.getInt(ARG_ID)

        if (id != null) {

            val mainActivity = activity as MainActivity

            mainActivity.manager.getRecipeDetails(recipeDetailsListener, id)
            mainActivity.manager.getEquipmentDetails(equipmentDetailsListener,id)
            mainActivity.manager.getSimilarRecipes(similarRecipeResponseListener, id)

        }

        return view
    }

    private val recipeDetailsListener:RecipeDetailsListener = object :RecipeDetailsListener{
        @SuppressLint("SetTextI18n")
        override fun didFetch(response: RecipeDetailsResponse?, message: String?) {
            response?.let {
                recipeTitle.text = it.title
                RecipeMealSummary.text = it.summary
                recipeMealTime.text = it.readyInMinutes.toString() + "min"
                recipeMealServing.text = it.servings.toString()
                recipeMealPrice.text = it.pricePerServing.toString()
                recipeMealNutrition.text = it.gaps
                recipeMealBadNutrition.text = it.spoonacularScore.toString()
                recipeMealGoodNutrition.text = it.healthScore.toString()


                Picasso.get().load(response.image).into(recipeImage)

                recipeRecyclerMealIngredients.setHasFixedSize(true)
                recipeRecyclerMealIngredients.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

                recipeIngredientsAdapter = IngredientsAdapter(activity, it.extendedIngredients)
                recipeRecyclerMealIngredients.adapter = recipeIngredientsAdapter
            }
        }

        override fun didError(message: String?) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

    }

    private val equipmentDetailsListener:EquipmentDetailsListener = object :EquipmentDetailsListener{
        override fun didFetch(response: Step?, message: String?) {
            recipeRecyclerMealEquipment.setHasFixedSize(true)
            recipeRecyclerMealEquipment.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            recipeEquipmentsAdapter = EquipmentsAdapter(activity, response!!.equipment)
            recipeRecyclerMealEquipment.adapter = recipeEquipmentsAdapter

        }

        override fun didError(message: String?) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

    }

    private val similarRecipeResponseListener: SimilarRecipeResponseListener =
        object : SimilarRecipeResponseListener {

            override fun didFetch(response: MutableList<SimilarRecipeResponse>?, message: String?) {

                similarRecipeRecycler.setHasFixedSize(true)
                similarRecipeRecycler.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                similarRecipeAdapter = SimilarRecipeAdapter(activity, response, recipeClickListener)
                similarRecipeRecycler.adapter = similarRecipeAdapter

            }


            override fun didError(message: String) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

    private val recipeClickListener =
        RecipeClickListener { id ->
            val recipeDetailsFragment = RecipeDetailsFragment()

            // using bundle to pass the data
            val bundle = Bundle().apply {
                putInt("id", id.toInt())
            }

            recipeDetailsFragment.arguments = bundle

            dismiss()
            (activity as MainActivity).loadFragment(recipeDetailsFragment)

//            val bottomNave = activity as MainActivity.bottomNav
        }


    private fun initializeViews(view: View) {
        recipeTitle = view.findViewById(R.id.recipeTitle)
        backToHome = view.findViewById(R.id.backToHome)
        fabRecipe = view.findViewById(R.id.fabRecipe)

        imageLL = view.findViewById(R.id.imageLL)
        recipeImage = view.findViewById(R.id.recipeImage)
        recipeMealTime = view.findViewById(R.id.recipeMealTime)
        recipeMealServing = view.findViewById(R.id.recipeMealServing)
        recipeMealPrice = view.findViewById(R.id.recipeMealPrice)

        btnIngredients = view.findViewById(R.id.btnIngredients)

        ingredientsLL = view.findViewById(R.id.ingredientsLL)
        IngredientsDropdown = view.findViewById(R.id.IngredientsDropdown)
        recipeRecyclerMealIngredients = view.findViewById(R.id.recipeRecyclerMealIngredients)
        btnFullDetail = view.findViewById(R.id.btnFullDetail)

        detail_n_Similar = view.findViewById(R.id.detail_n_Similar)
        recipeDropdown = view.findViewById(R.id.recipeDropdown)
        fullRecipeRL = view.findViewById(R.id.fullRecipeRL)
        fullRecipeSV = view.findViewById(R.id.fullRecipeSV)

        recipeRecyclerMealEquipment = view.findViewById(R.id.recipeRecyclerMealEquipment)
        RecipeMealSummary = view.findViewById(R.id.RecipeMealSummary)

        recipeMealNutrition = view.findViewById(R.id.recipeMealNutrition)
        recipeMealBadNutrition = view.findViewById(R.id.recipeMealBadNutrition)
        recipeMealGoodNutrition = view.findViewById(R.id.recipeMealGoodNutrition)

        nutriDropdown1 = view.findViewById(R.id.nutriDropdown1)
        nutriDropup1 = view.findViewById(R.id.nutriDropup1)
        nutriBadDropdown1 = view.findViewById(R.id.nutriBadDropdown1)
        nutriBadDropup1 = view.findViewById(R.id.nutriBadDropup1)
        nutriGoodDropdown1 = view.findViewById(R.id.nutriGoodDropdown1)
        nutriGoodDropup1 = view.findViewById(R.id.nutriGoodDropup1)

        btnSimilarRecipe = view.findViewById(R.id.btnSimilarRecipe)
        similarRecipeRecycler = view.findViewById(R.id.similarRecipeRecycler)
        similarRecipeDropdown  =view.findViewById(R.id.similarRecipeDropdown)
        similarRecipe = view.findViewById(R.id.similarRecipe)

    }

    private fun setupClickListeners() {
        backToHome.setOnClickListener {
            dismiss()
            val mainActivity = activity as MainActivity
            (mainActivity).loadFragment(SearchFragment())
            
        }

        fabRecipe.setOnClickListener {
            Toast.makeText(activity,"Saved",Toast.LENGTH_SHORT).show()
            fabRecipe.setImageResource(R.drawable.fav_filled)
        }

        btnIngredients.setOnClickListener {
            imageLL.visibility = View.GONE
            ingredientsLL.visibility = View.VISIBLE
        }

        IngredientsDropdown.setOnClickListener {
            recipeRecyclerMealIngredients.visibility = View.VISIBLE
            btnFullDetail.visibility = View.VISIBLE

//            fullRecipeRL.visibility = View.GONE
//            fullRecipeSV.visibility = View.GONE
//
//            btnSimilarRecipe.visibility = View.GONE
//            similarRecipeRecycler.visibility = View.GONE

            detail_n_Similar.visibility = View.GONE

        }

        btnFullDetail.setOnClickListener {
            recipeRecyclerMealIngredients.visibility = View.GONE

            detail_n_Similar.visibility = View.VISIBLE
            fullRecipeRL.visibility = View.VISIBLE
            fullRecipeSV.visibility = View.VISIBLE
            btnFullDetail.visibility = View.GONE

            btnSimilarRecipe.visibility = View.VISIBLE
        }

        recipeDropdown.setOnClickListener {
            recipeRecyclerMealIngredients.visibility = View.GONE
            btnFullDetail.visibility= View.GONE

            detail_n_Similar.visibility = View.VISIBLE
            fullRecipeSV.visibility = View.VISIBLE
            btnSimilarRecipe.visibility = View.VISIBLE

            similarRecipeRecycler.visibility = View.GONE
            similarRecipe.visibility = View.GONE
        }

        btnSimilarRecipe.setOnClickListener {
            btnSimilarRecipe.visibility = View.GONE
            fullRecipeSV.visibility = View.GONE


            similarRecipe.visibility = View.VISIBLE
            similarRecipeRecycler.visibility = View.VISIBLE
        }


        nutriDropdown1.setOnClickListener {
            nutriDropdown1.visibility = View.GONE
            nutriDropup1.visibility = View.VISIBLE
            recipeMealNutrition.visibility = View.VISIBLE
        }

        nutriDropup1.setOnClickListener {
            nutriDropdown1.visibility = View.VISIBLE
            nutriDropup1.visibility = View.GONE
            recipeMealNutrition.visibility = View.GONE
        }

        nutriBadDropdown1.setOnClickListener {
            nutriBadDropdown1.visibility = View.GONE
            nutriBadDropup1.visibility = View.VISIBLE
            recipeMealBadNutrition.visibility = View.VISIBLE
        }

        nutriBadDropup1.setOnClickListener {
            nutriBadDropdown1.visibility = View.VISIBLE
            nutriBadDropup1.visibility = View.GONE
            recipeMealBadNutrition.visibility = View.GONE
        }

        nutriGoodDropdown1.setOnClickListener {
            nutriGoodDropdown1.visibility = View.GONE
            nutriGoodDropup1.visibility = View.VISIBLE
            recipeMealGoodNutrition.visibility = View.VISIBLE
        }

        nutriGoodDropup1.setOnClickListener {
            nutriGoodDropdown1.visibility = View.VISIBLE
            nutriGoodDropup1.visibility = View.GONE
            recipeMealGoodNutrition.visibility = View.GONE
        }

    }

}