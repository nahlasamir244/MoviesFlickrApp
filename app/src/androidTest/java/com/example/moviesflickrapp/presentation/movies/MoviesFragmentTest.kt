package com.example.moviesflickrapp.presentation.movies

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.base.utils.livedata.SingleLiveEvent
import com.example.moviesflickrapp.util.espressoutils.RecyclerViewMatcher
import com.example.moviesflickrapp.util.rules.FragmentTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val fragmentRule = FragmentTestRule(MoviesFragment::class.java)

    @get:Rule(order = 2)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val states = MediatorLiveData<MoviesScreenContract.State>()
    private val events = SingleLiveEvent<MoviesScreenContract.Event>()

    private val viewModel = mock<MoviesViewModel>()

    private fun injectIntent() = Intent()

    private fun launchActivity() {
        fragmentRule.launchActivity(injectIntent())
    }

    @Before
    fun setup() {
        whenever(viewModel.moviesState).doReturn(states)
        whenever(viewModel.moviesEvent).doReturn(events)
    }

    @Test
    fun whenSuccessState_shouldDisplayListOfMovies() {
        launchActivity()
        val uiModelList = mockUiModelList()
        runBlocking(Dispatchers.Main) {
            states.postValue(
                MoviesScreenContract.State.Success(
                    uiModelList
                )
            )
        }
        onView(withId(R.id.textView_errorMessage)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.progressBar)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.button_retry)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.recyclerView_movies)).check(
            matches(
                isDisplayed()
            )
        )
        onView(
            RecyclerViewMatcher()
                .atPositionOnView(0, R.id.materialText_owner, R.id.recyclerView_movies)
        ).check(matches(isDisplayed()))
        onView(
            RecyclerViewMatcher()
                .atPositionOnView(0, R.id.materialText_owner, R.id.recyclerView_movies)
        ).check(matches(withText(("yellow"))))

        onView(
            RecyclerViewMatcher()
                .atPositionOnView(1, R.id.imageView_photoImage, R.id.recyclerView_movies)
        ).check(matches(isDisplayed()))

        onView(
            RecyclerViewMatcher()
                .atPositionOnView(1, R.id.textView_photoTitle, R.id.recyclerView_movies)
        ).check(matches(withText(("hello"))))

    }


    @Test
    fun whenMovieClicked_shouldTriggerOnMovieClickedAction() {
        launchActivity()

        val uiModelList = mockUiModelList()
        runBlocking(Dispatchers.Main) {
            states.postValue(
                MoviesScreenContract.State.Success(
                    uiModelList
                )
            )
        }
        onView(
            RecyclerViewMatcher().atPositionOnView(
                1,
                R.id.cardView,
                R.id.recyclerView_movies
            )
        ).perform(
            ViewActions.click()
        )
        verify(viewModel, times(1)).invokeAction(
            MoviesScreenContract.Action.OnMovieClicked(
                PhotoUIModel(
                    id = "123",
                    title = "hello",
                    imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
                )
            )
        )
    }

    @Test
    fun whenRetryClicked_shouldTriggerSearchMoviesAction() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(
                MoviesScreenContract.State.Error(
                    "Error"
                )
            )
        }
        onView(withId(R.id.textInputEditText_search)).perform(typeText("query"))
        onView(
            withId(R.id.button_retry)
        ).perform(
            ViewActions.click()
        )
        verify(viewModel, times(1)).invokeAction(
            MoviesScreenContract.Action.SearchMovies(
                "query"
            )
        )
    }

    @Test
    fun whenLoadingState_shouldShowLoading() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(MoviesScreenContract.State.Loading)
        }
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_errorMessage)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.button_retry)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.recyclerView_movies)).check(matches(Matchers.not(isDisplayed())))
    }

    @Test
    fun whenEmptyState_shouldShowEmptySearchResultScreen() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(MoviesScreenContract.State.EmptySearchResult)
        }

        onView(withId(R.id.progressBar)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.textView_errorMessage)).check(matches(isDisplayed()))
        onView(withId(R.id.button_retry)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView_movies)).check(matches(Matchers.not(isDisplayed())))

    }

    @Test
    fun whenErrorState_shouldShowErrorScreen() {
        launchActivity()

        runBlocking(Dispatchers.Main) {
            states.postValue(MoviesScreenContract.State.Error("Error"))
        }

        onView(withId(R.id.progressBar)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.textView_errorMessage)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_errorMessage)).check(matches(withText("Error")))
        onView(withId(R.id.button_retry)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView_movies)).check(matches(Matchers.not(isDisplayed())))
    }

    private fun mockUiModelList() = listOf(
        OwnerUIModel("yellow"),
        PhotoUIModel(
            id = "123",
            title = "hello",
            imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
        ),
        PhotoUIModel(
            id = "123",
            title = "hello",
            imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
        ),
        OwnerUIModel("nahla"),
        PhotoUIModel(
            id = "123",
            title = "nahla",
            imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
        ),
        PhotoUIModel(
            id = "123",
            title = "nahla",
            imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
        ),
    )

    @After
    fun tearDown() {
        clearAllMocks()
    }
}
