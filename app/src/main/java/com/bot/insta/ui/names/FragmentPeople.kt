package com.bot.insta.ui.names

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bot.insta.App
import com.bot.insta.R
import com.bot.insta.internal.log
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import kotlinx.android.synthetic.main.fragment_people.view.*

class FragmentPeople : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)

        view.apply {
            et_tag.setText(App.listOfAccounts)

            et_tag.addChipTerminator(   '\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
        }

        return view
    }

    override fun onStop() {
        super.onStop()
        view?.et_tag?.chipValues?.let {
            log(it)
            App.listOfAccounts = it
        }
    }

}