package com.dicoding.githubusers1.ui.detail.fragmenthing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers1.R
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.databinding.FragmentDetailBinding
import com.dicoding.githubusers1.ui.MainAdapter
import com.dicoding.githubusers1.ui.detail.DetailUserActivity
import com.dicoding.githubusers1.ui.detail.DetailUserViewModel
import com.dicoding.githubusers1.utils.Hasil

class DetailUserFragment : Fragment() {

    private var binding: FragmentDetailBinding? = null
    private lateinit var tvNoUser: TextView
    private lateinit var imgNoUser: ImageView
    private val adapter by lazy {
        MainAdapter { user ->
            startActivity(Intent(requireContext(), DetailUserActivity::class.java).apply {
                putExtra("item", user)
            })
        }
    }

    companion object {
        const val FOLLOWING = 101
        const val FOLLOWERS = 100

        fun newInstance(type: Int): DetailUserFragment {
            return DetailUserFragment().apply {
                this.type = type
            }
        }
    }

    private fun manageHasilFollow(state: Hasil) {
        when (state) {
            is Hasil.Success<*> -> {
                val userList = state.data as MutableList<User>
                if (userList.isEmpty()) {
                    showEmptyState()
                } else {
                    hideEmptyState()
                    adapter.setGithubUserData(userList)
                }
            }
            is Hasil.Error -> {
                showError(state.exception.message.toString())
            }
            is Hasil.Loading -> {
                showLoading(state.isLoading)
            }
        }
    }

    private val viewModel by activityViewModels<DetailUserViewModel>()
    private var type = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        binding?.let { b ->
            tvNoUser = b.root.findViewById(R.id.tv_no_user)
            imgNoUser = b.root.findViewById(R.id.img_no_user)
            setupRecyclerView()
        }

        viewModel.resultUserFollowers.observe(viewLifecycleOwner, this::manageHasilFollow)
        viewModel.resultUserFollowing.observe(viewLifecycleOwner, this::manageHasilFollow)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    private fun setupRecyclerView() {
        binding?.rvFollows?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvFollows?.setHasFixedSize(true)
        binding?.rvFollows?.adapter = adapter
    }

    private fun showEmptyState() {
        tvNoUser.visibility = View.VISIBLE
        imgNoUser.visibility = View.VISIBLE
        binding?.rvFollows?.visibility = View.GONE
    }

    private fun hideEmptyState() {
        tvNoUser.visibility = View.GONE
        imgNoUser.visibility = View.GONE
        binding?.rvFollows?.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.isVisible = isLoading
    }
}
