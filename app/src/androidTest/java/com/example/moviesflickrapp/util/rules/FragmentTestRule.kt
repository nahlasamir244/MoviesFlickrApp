package com.example.moviesflickrapp.util.rules

import android.R
import androidx.fragment.app.Fragment
import androidx.test.rule.ActivityTestRule
import com.example.moviesflickrapp.presentation.util.TestActivity
import org.junit.Assert

class FragmentTestRule<F : Fragment>(private val mFragmentClass: Class<F>) :
    ActivityTestRule<TestActivity>(TestActivity::class.java, true, false) {

    lateinit var fragment: F
    override fun afterActivityLaunched() {
        super.afterActivityLaunched()

        activity.runOnUiThread {
            try {
                // Instantiate and insert the fragment into the container layout
                val manager = activity.supportFragmentManager
                val transaction = manager.beginTransaction()
                fragment = mFragmentClass.newInstance()
                fragment.arguments = activity.intent.extras
                transaction.replace(R.id.content, fragment)
                transaction.commit()
            } catch (e: InstantiationException) {
                Assert.fail(
                    String.format(
                        "%s: Could not insert %s into TestActivity: %s",
                        javaClass.simpleName,
                        mFragmentClass.simpleName,
                        e.message
                    )
                )
            } catch (e: IllegalAccessException) {
                Assert.fail(
                    String.format(
                        "%s: Could not insert %s into TestActivity: %s",
                        javaClass.simpleName,
                        mFragmentClass.simpleName,
                        e.message
                    )
                )
            }
        }
    }
}
