package com.sunny.flavorfinder.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import com.sunny.flavorfinder.MainActivity
import com.sunny.flavorfinder.R
import com.sunny.flavorfinder.RequestManager
import com.sunny.flavorfinder.adapterJ.EquipmentsAdapter
import com.sunny.flavorfinder.adapterJ.IngredientsAdapter
import com.sunny.flavorfinder.listenersJ.EquipmentDetailsListener
import com.sunny.flavorfinder.listenersJ.RecipeDetailsListener
import com.sunny.flavorfinder.modelJ.RecipeDetailsResponse
import com.sunny.flavorfinder.modelJ.Step


class RecipeDetailsFragment : Fragment() {

//    var id by Delegates.notNull<Int>()
    lateinit var textt:TextView

    lateinit var textView_meal_name :TextView
    lateinit var textView_meal_summary :TextView
    lateinit var textView_meal_time :TextView
    lateinit var textView_meal_serving :TextView
    lateinit var textView_meal_price :TextView

    lateinit var imageView_meal_image : ImageView

    lateinit var recycler_meal_ingredients:RecyclerView

    lateinit var recycler_meal_equipment:RecyclerView

    lateinit var manager:RequestManager

    lateinit var dialog:ProgressDialog

    lateinit var ingredientsAdapter: IngredientsAdapter

    lateinit var equipmentsAdapter: EquipmentsAdapter

    lateinit var homeBack:ImageView

    lateinit var bottomNav : BottomNavigationView

    lateinit var textView_meal_nutrition: TextView
    lateinit var textView_meal_bad_nutrition: TextView
    lateinit var textView_meal_good_nutrition: TextView

    lateinit var nutriDropdown:ImageView
    lateinit var nutriDropup:ImageView
    lateinit var nutriBadDropdown:ImageView
    lateinit var nutriBadDropup:ImageView
    lateinit var nutriGoodDropdown:ImageView
    lateinit var nutriGoodDropup:ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_details, container, false)

        val id = arguments?.getInt("id")

//        textt = view.findViewById(R.id.textView_blank)

//        textt.text = id.toString()

        manager = activity?.let { RequestManager(it) }!!
        if (id != null) {
            manager.getRecipeDetails(recipeDetailsListener, id)
            manager.getEquipmentDetails(equipmentDetailsListener, id)
        }
        dialog = ProgressDialog(activity)
        dialog.setTitle("Loading...")
        dialog.show()

        textView_meal_name = view.findViewById(R.id.textView_meal_name)
//        textView_meal_source = findViewById<TextView>(R.id.textView_meal_source)
        textView_meal_summary = view.findViewById(R.id.textView_meal_summary)

        textView_meal_time = view.findViewById(R.id.textView_meal_time)
        textView_meal_serving = view.findViewById(R.id.textView_meal_serving)
        textView_meal_price = view.findViewById(R.id.textView_meal_price)


        imageView_meal_image = view.findViewById(R.id.imageView_meal_image)

        homeBack = view.findViewById(R.id.homeBack)

        homeBack.setOnClickListener {
            val homeFragment = HomeDashboard()
            val mainActivity = activity as MainActivity
            (mainActivity).loadFragment(homeFragment)
            bottomNav = mainActivity.findViewById(R.id.bottomNav)
            bottomNav.visibility = View.VISIBLE

        }

        textView_meal_nutrition = view.findViewById(R.id.textView_meal_nutrition)
        textView_meal_bad_nutrition = view.findViewById(R.id.textView_meal_bad_nutrition)
        textView_meal_good_nutrition = view.findViewById(R.id.textView_meal_good_nutrition)

        nutriDropdown = view.findViewById(R.id.nutriDropdown)
        nutriDropup = view.findViewById(R.id.nutriDropup)
        nutriBadDropdown = view.findViewById(R.id.nutriBadDropdown)
        nutriBadDropup = view.findViewById(R.id.nutriBadDropup)
        nutriGoodDropdown = view.findViewById(R.id.nutriGoodDropdown)
        nutriGoodDropup = view.findViewById(R.id.nutriGoodDropup)

        nutriDropdown.setOnClickListener {
            nutriDropdown.visibility = View.GONE
            nutriDropup.visibility = View.VISIBLE
            textView_meal_nutrition.visibility = View.VISIBLE
        }

        nutriDropup.setOnClickListener {
            nutriDropdown.visibility = View.VISIBLE
            nutriDropup.visibility = View.GONE
            textView_meal_nutrition.visibility = View.GONE
        }

        nutriBadDropdown.setOnClickListener {
            nutriBadDropdown.visibility = View.GONE
            nutriBadDropup.visibility = View.VISIBLE
            textView_meal_bad_nutrition.visibility = View.VISIBLE
        }

        nutriBadDropup.setOnClickListener {
            nutriBadDropdown.visibility = View.VISIBLE
            nutriBadDropup.visibility = View.GONE
            textView_meal_bad_nutrition.visibility = View.GONE
        }

        nutriGoodDropdown.setOnClickListener {
            nutriGoodDropdown.visibility = View.GONE
            nutriGoodDropup.visibility = View.VISIBLE
            textView_meal_good_nutrition.visibility = View.VISIBLE
        }

        nutriGoodDropup.setOnClickListener {
            nutriGoodDropdown.visibility = View.VISIBLE
            nutriGoodDropup.visibility = View.GONE
            textView_meal_good_nutrition.visibility = View.GONE
        }

        recycler_meal_ingredients = view.findViewById(R.id.recycler_meal_ingredients)

        recycler_meal_equipment = view.findViewById(R.id.recycler_meal_equipment)

        return view;
    }


    private val recipeDetailsListener: RecipeDetailsListener = object : RecipeDetailsListener {
        @SuppressLint("SetTextI18n")
        override fun didFetch(response: RecipeDetailsResponse, message: String) {
            dialog.dismiss()
            textView_meal_name.text = response.title
//            textView_meal_source.setText(response.sourceName)
            textView_meal_summary.text = response.summary
            textView_meal_time.text = response.readyInMinutes.toString() + "min"
            textView_meal_price.text = response.pricePerServing.toString()
            textView_meal_serving.text = response.servings.toString()

            textView_meal_nutrition.text = response.gaps
            textView_meal_good_nutrition.text = response.healthScore.toString()
            textView_meal_bad_nutrition.text = response.spoonacularScore.toString()


            Picasso.get().load(response.image).into(imageView_meal_image)

            recycler_meal_ingredients.setHasFixedSize(true)
            recycler_meal_ingredients.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            ingredientsAdapter = IngredientsAdapter(activity, response.extendedIngredients)
            recycler_meal_ingredients.adapter = ingredientsAdapter


        }

        override fun didError(message: String) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private val equipmentDetailsListener :EquipmentDetailsListener = object: EquipmentDetailsListener{
        override fun didFetch(response: Step?, message: String?) {
            recycler_meal_equipment.setHasFixedSize(true)
            recycler_meal_equipment.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


            equipmentsAdapter = EquipmentsAdapter(activity, response!!.equipment)

            recycler_meal_equipment.adapter = equipmentsAdapter
        }

        override fun didError(message: String?) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

    }




}