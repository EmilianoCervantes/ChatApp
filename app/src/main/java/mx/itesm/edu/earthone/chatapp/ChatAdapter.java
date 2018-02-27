package mx.itesm.edu.earthone.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ovman on 27/02/2018.
 */

public class ChatAdapter extends ArrayAdapter <ChatPojo> {
    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<ChatPojo> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatPojo chatPojo = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_layout,parent,false);
        }
        TextView author = (TextView)convertView.findViewById(R.id.autor);
        TextView message = (TextView)convertView.findViewById(R.id.mensaje);
        author.setText(chatPojo.getName());
        message.setText(chatPojo.getMessage());

        return convertView;
    }
}
