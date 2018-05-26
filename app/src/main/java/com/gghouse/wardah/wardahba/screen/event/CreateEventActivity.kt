package com.gghouse.wardah.wardahba.screen.event

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.gghouse.wardah.wardahba.R

class CreateEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_event, menu)
        return true
    }
}
