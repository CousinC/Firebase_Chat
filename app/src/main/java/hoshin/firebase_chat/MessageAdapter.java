package hoshin.firebase_chat;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Listener mListener;
    private List<Message> mMessagesList;
    private User mCurrentUser;

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 0;

    public MessageAdapter(Listener listener, List<Message> data, User user) {
        this.mListener = listener;
        this.mMessagesList = data;
        this.mCurrentUser = user;
    }

    public void setData(List<Message> messageList) {
        mMessagesList = messageList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_messages, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_messages, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mMessagesList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        int itemType;
        Message message = mMessagesList.get(position);
        String userEmail = message.getUserEmail();
        if(userEmail.equals(mCurrentUser.getEmail())){
            itemType = TYPE_SENT;
        }
        else{
            itemType = TYPE_RECEIVED;
        }
        return itemType;
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ImageView mUserAvatar;
        TextView mUserName;
        TextView mMessageContent;
        RelativeTimeTextView mMessageTime;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);

            mUserAvatar = itemView.findViewById(R.id.imgv_userAvatar);
            mUserName = itemView.findViewById(R.id.txv_userName);
            mMessageContent = itemView.findViewById(R.id.txv_messageContent);
            mMessageTime = itemView.findViewById(R.id.rtxv_messageTime);
        }

        void setData(Message message) {
            mUserName.setText(message.getUserName());
            mMessageContent.setText(message.getContent());
            mMessageTime.setReferenceTime(message.getTimestamp());

            if(!TextUtils.isEmpty(message.getUserEmail())){
                Glide
                        .with(mUserAvatar.getContext())
                        .load(Constant.GRAVATAR_PREFIX + Utils.md5(message.getUserEmail()))
                        .apply(RequestOptions.circleCropTransform())
                        .into(mUserAvatar);
            }
            else{
                mUserAvatar.setImageResource(R.color.colorAccent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onItemLongClick(getAdapterPosition(), mMessagesList.get(getAdapterPosition()));
            return true;
        }
    }

    public interface Listener {
        void onItemLongClick(int position,Message message);
    }

}