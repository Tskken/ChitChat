package com.example.tsuki.chit_chat.CONTROLLER

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.tsuki.chit_chat.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get fragment manager //
        val fm = supportFragmentManager

        // Get fragment for ID //
        var fragment: Fragment? = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            // create new Bundle //
            val bundle = Bundle()

            // establish fragment //
            fragment = MessageRecyclerController()

            // pass bundle data to fragment as argument //
            fragment.arguments = bundle


            // Create intent from fragment //
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }


}
