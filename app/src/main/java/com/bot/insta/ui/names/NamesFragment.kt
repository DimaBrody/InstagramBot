package com.bot.insta.ui.names

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bot.insta.R
import com.bot.insta.components.navigation.Navigation
import com.bot.insta.components.navigation.navigateMain
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_names.view.*
import java.util.*

class NamesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_names, container, false)

        view.initViews()

        return view
    }

    private fun View.initViews() {
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP)
        toolbar.title = "People"
        toolbar.setOnClickListener {
            Navigation.processBack(::navigateMain)
        }

        val viewPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        viewPagerAdapter.addFragment(FragmentPeople(),"People")
        viewPagerAdapter.addFragment(FragmentHashtag(),"Hashtag")

        view_pager.adapter = viewPagerAdapter

        tab_layout.setupWithViewPager(view_pager)



        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_people)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_hashtag)

        // set icon color pre-selected
        // set icon color pre-selected
        tab_layout.getTabAt(0)!!.icon!!.setColorFilter(
            resources.getColor(R.color.deep_orange_500),
            PorterDuff.Mode.SRC_IN
        )
        tab_layout.getTabAt(1)!!.icon?.setColorFilter(
            resources.getColor(R.color.grey_60),
            PorterDuff.Mode.SRC_IN
        )

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                toolbar.title = viewPagerAdapter.getTitle(tab.position)
                tab.icon!!.setColorFilter(
                    resources.getColor(R.color.deep_orange_500),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setColorFilter(
                    resources.getColor(R.color.grey_60),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private class SectionsPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> =
            ArrayList()
        private val mFragmentTitleList: MutableList<String> =
            ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int = mFragmentList.size


        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        fun getTitle(position: Int): String {
            return mFragmentTitleList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

}