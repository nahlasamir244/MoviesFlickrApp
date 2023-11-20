package com.example.moviesflickrapp.util.rules

import android.R
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.example.moviesflickrapp.presentation.util.TestActivity
import org.junit.Assert
import org.junit.rules.ExternalResource

class FragmentScenarioTestRule<F : Fragment>(private val mFragmentClass: Class<F>) :
    ExternalResource() {

    private lateinit var fragment: F
    lateinit var activity: TestActivity

    private var scenarioSupplier: () -> ActivityScenario<TestActivity>

    private var scenario: ActivityScenario<TestActivity>? = null

    private var scenarioLaunched: Boolean = false

    override fun after() {
        scenario?.close()
    }

    fun launch(newIntent: Intent? = null) {
        if (scenarioLaunched) throw IllegalStateException("Scenario has already been launched!")

        newIntent?.let { scenarioSupplier = { ActivityScenario.launch(it) } }

        scenario = scenarioSupplier()
        scenarioLaunched = true

        scenario?.onActivity {
            activity = it
            runOnUiThread {
                try {
                    // Instantiate and insert the fragment into the container layout
                    val manager = it.supportFragmentManager
                    val transaction = manager.beginTransaction()
                    fragment = mFragmentClass.newInstance()
                    fragment.arguments = it.intent.extras
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

        fun getScenario(): ActivityScenario<TestActivity> = checkNotNull(scenario)
    }

    init {
        scenarioSupplier = { ActivityScenario.launch(TestActivity::class.java) }
    }
}
