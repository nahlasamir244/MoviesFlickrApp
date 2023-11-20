package com.example.moviesflickrapp.util.rules

import android.R
import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.test.rule.ActivityTestRule
import org.junit.Assert

class FragmentTestRuleWithDynamicActivity<F : Fragment, A : Activity>(
    private val mFragmentClass: Class<F>,
    mActivityClass: Class<A>
) : ActivityTestRule<A>(mActivityClass, true, false) {

    lateinit var fragment: F
    override fun afterActivityLaunched() {
        super.afterActivityLaunched()

        activity.runOnUiThread {
            try {
                // Instantiate and insert the fragment into the container layout
                val manager = (activity as FragmentActivity).supportFragmentManager
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
