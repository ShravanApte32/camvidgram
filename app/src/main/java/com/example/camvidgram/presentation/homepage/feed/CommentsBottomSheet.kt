package com.example.camvidgram.presentation.homepage.feed

import CommentsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camvidgram.databinding.BottomSheetCommentsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CommentsBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetCommentsBinding? = null
    private val binding get() = _binding!!

    private val adapter = CommentsAdapter()

    private val viewModel: CommentsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCommentsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog ?: return
        val bottomSheet = dialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        ) ?: return

        bottomSheet.layoutParams = bottomSheet.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
        behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = requireArguments().getString(ARG_POST_ID)!!

        setupRecycler()
        observeComments(postId)
        setupSendComments(postId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }



    private fun setupRecycler() {
        binding.commentsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CommentsBottomSheet.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeComments(postId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getComments(postId).collect { comments ->
                    Timber.d("UI received ${comments.size} comments")
                    adapter.submitList(comments.toList())
                }
            }
        }
    }


    private fun setupSendComments(postId: String) {
        binding.sendButton.setOnClickListener {
            val text = binding.commentInput.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.addComment(postId, text)
                binding.commentInput.text?.clear()
            }
        }
    }


    companion object{
        private const val ARG_POST_ID = "post_id"

        fun newInstance(postId: String) = CommentsBottomSheet().apply {
            arguments = bundleOf(ARG_POST_ID to postId)
        }
    }
}