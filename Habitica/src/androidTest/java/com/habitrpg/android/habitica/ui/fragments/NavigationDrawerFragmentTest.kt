package com.habitrpg.android.habitica.ui.fragments

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.LargeTest
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.databinding.DrawerMainBinding
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KTextView
import io.mockk.spyk
import org.hamcrest.Matcher
import org.junit.Test

class SectionHeaderItem(parent: Matcher<View>) : KRecyclerItem<SectionHeaderItem>(parent) {
    val title = KTextView(parent) { isRoot() }
}

class MainItem(parent: Matcher<View>) : KRecyclerItem<MainItem>(parent) {
    val title = KTextView(parent) { withId(R.id.titleTextView) }
}

class NavigationDrawerScreen : Screen<NavigationDrawerScreen>() {
    val recycler: KRecyclerView = KRecyclerView({
        withId(R.id.recyclerView)
    }, itemTypeBuilder = {
        itemType(::SectionHeaderItem)
        itemType(::MainItem)
    })
}

@LargeTest
internal class NavigationDrawerFragmentTest : FragmentTestCase<NavigationDrawerFragment, DrawerMainBinding, NavigationDrawerScreen>() {
    override fun makeFragment() {
        scenario = launchFragmentInContainer(null, R.style.MainAppTheme) {
            fragment = spyk()
            fragment.userRepository = userRepository
            fragment.inventoryRepository = inventoryRepository
            fragment.socialRepository = socialRepository
            fragment.contentRepository = contentRepository
            fragment.configManager = appConfigManager
            return@launchFragmentInContainer fragment
        }
    }

    override val screen = NavigationDrawerScreen()

    @Test
    fun showsMenuItems() {
        screen {
            recycler {
                isVisible()
                childWith<MainItem> { withDescendant { withText("Tasks") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Market") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Items") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Purchase Gems") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Subscription") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Party") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Tavern") } }.isVisible()
                childWith<MainItem> { withDescendant { withText("Support") } }.isVisible()
            }
        }
    }
}