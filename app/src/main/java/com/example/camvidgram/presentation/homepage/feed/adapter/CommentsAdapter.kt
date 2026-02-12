import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.camvidgram.R
import com.example.camvidgram.databinding.ItemCommentBinding
import com.example.camvidgram.domain.models.Comment

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.CommentVH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentVH(binding)
    }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentVH(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.username.text = comment.userName
            binding.commentText.text = comment.text
            binding.time.text = comment.time
        }
    }

    class Diff : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(o: Comment, n: Comment) = o.id == n.id
        override fun areContentsTheSame(o: Comment, n: Comment) = o == n
    }
}
