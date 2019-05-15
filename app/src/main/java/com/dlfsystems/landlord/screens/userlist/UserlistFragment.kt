package com.dlfsystems.landlord.screens.userlist

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlfsystems.landlord.R
import com.dlfsystems.landlord.data.FirebaseRepository
import com.dlfsystems.landlord.data.model.User
import com.dlfsystems.landlord.screens.base.BaseFragment
import com.dlfsystems.landlord.screens.base.BaseState
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.fragment_userlist.*

class UserlistFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_userlist
    override val presenter = UserlistPresenter(this)
    override fun defaultState() = UserlistState()

    fun state() = stateHolder.state.value as UserlistState

    val repo = FirebaseRepository()

    lateinit var recyclerAdapter: UserlistRecyclerAdapter

    override fun subscribeUI(view: View) {
        userlist_recyclerview.layoutManager = LinearLayoutManager(context)
        val options = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(repo.getUsers(), User::class.java)
            .setLifecycleOwner(this)
            .build()
        recyclerAdapter = UserlistRecyclerAdapter(options, actions)
        userlist_recyclerview.adapter = recyclerAdapter

    }

    override fun render(state: BaseState) {
        state as UserlistState

    }

}