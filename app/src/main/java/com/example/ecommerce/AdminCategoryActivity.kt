package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin_category.*

class AdminCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)

        t_shirts.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "tShirts")
                startActivity(intent)

        }


        sports_t_shirts.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Sports tShirts")
                startActivity(intent)

        }


        female_dresses.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Female Dresses")
                startActivity(intent)

        }


        sweathers.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Sweathers")
                startActivity(intent)

        }


        glasses.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Glasses")
                startActivity(intent)

        }


        hats_caps.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Hats Caps")
                startActivity(intent)

        }


        purses_bags_wallets.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Wallets Bags Purses")
                startActivity(intent)

        }


        shoes.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Shoes")
                startActivity(intent)

        }


        headphones_handfree.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "HeadPhones HandFree")
                startActivity(intent)

        }


        laptop_pc.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Laptops")
                startActivity(intent)

        }


        watches.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Watches")
                startActivity(intent)

        }


        mobilephones.setOnClickListener {

                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Mobile Phones")
                startActivity(intent)

        }
    }
}