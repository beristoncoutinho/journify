package com.example.journify;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.api.Context;

public class adapter extends FirestoreRecyclerAdapter<Note, adapter.NoteViewHolder> {
MainActivity context;
    public adapter(@NonNull FirestoreRecyclerOptions<Note> options, MainActivity context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleText.setText((note.title));
        holder.contentText.setText((note.content));
        holder.timeStamp.setText(Utility.timeStampToString(note.timestamp));
        holder.itemView.setOnClickListener((v)->{
            Intent intent =new Intent(context,DiaryAdd.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
          TextView contentText;
            TextView timeStamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.textTitle);

            contentText=itemView.findViewById(R.id.textcontent);
           timeStamp=itemView.findViewById(R.id.textTime);
        }
    }
}
