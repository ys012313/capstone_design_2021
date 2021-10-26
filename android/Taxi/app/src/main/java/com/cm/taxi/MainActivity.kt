package com.cm.taxi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cm.taxi.util.transact
import com.cm.taxi.view.home.HomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview



@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.transact {
                add(R.id.root_container, HomeFragment())
            }
        }
    }
}
