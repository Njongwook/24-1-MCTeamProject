package com.example.timeconversionapplication

import HomeFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.timeconversionapplication.databinding.ActivityNaviBinding

private const val TAG_HOME = "home_fragment"
private const val TAG_PRODUCT = "product_fragment"
private const val TAG_CALENDAR = "calendar_fragment"
private const val TAG_TIPS = "tips_fragment"
private const val REQUEST_CODE_JOB_REGIS = 1
class NaviActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setFragment(TAG_HOME, HomeFragment())
        }

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.productFragment -> setFragment(TAG_PRODUCT, ProductFragment())
                R.id.calendarFragment -> setFragment(TAG_CALENDAR, CalendarFragment())
                R.id.tipsFragment -> setFragment(TAG_TIPS, TipsFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        val home = manager.findFragmentByTag(TAG_HOME)
        val product = manager.findFragmentByTag(TAG_PRODUCT)
        val calendar = manager.findFragmentByTag(TAG_CALENDAR)
        val tips = manager.findFragmentByTag(TAG_TIPS)

        if (home != null) {
            fragTransaction.hide(home)
        }
        if (product != null) {
            fragTransaction.hide(product)
        }
        if (calendar != null) {
            fragTransaction.hide(calendar)
        }
        if (tips != null) {
            fragTransaction.hide(tips)
        }

        val existingFragment = manager.findFragmentByTag(tag)
        if (existingFragment == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
            fragTransaction.show(fragment)
        } else {
            fragTransaction.show(existingFragment)

            // Refresh data if the fragment is HomeFragment
            if (existingFragment is HomeFragment) {
                existingFragment.refreshData()
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_JOB_REGIS && resultCode == Activity.RESULT_OK) {
            setFragment(TAG_HOME, HomeFragment.newInstance())
            binding.navigationView.selectedItemId = R.id.homeFragment // 홈 프래그먼트 선택
        }
    }
}
