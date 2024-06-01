package com.example.journify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class DiaryAdd extends AppCompatActivity {
EditText title ,content;
TextView save;

String EditTitle,EditContent,docId;
boolean isEditMode=false;
ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add);
        Gretting();

        ImageView close =findViewById(R.id.imageClose);
        close.setOnClickListener((v)->finish());
        title=findViewById(R.id.TitleText);
        content=findViewById(R.id.ContentText);
        save=findViewById(R.id.textSave);
        save.setOnClickListener((v)->save());
        delete=findViewById(R.id.delete_btn);
        EditTitle=getIntent().getStringExtra("title");
        EditContent=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
            delete.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener((v)-> deleteIt() );
        title.setText(EditTitle);
        content.setText(EditContent);
    }
    private void deleteIt() {

        deleteDataFromFirebase();
        finish();
    }
    private void save() {
        String Title=title.getText().toString();
        String Content=content.getText().toString();
        if(Title==null || Title.isEmpty()){
            title.setError("Title is required");
            return;
        }
      diary d=new diary();
        d.setTitle(Title);
        d.setContent(Content);
        d.setTimestamp(Timestamp.now());
        saveDiaryToFirebase(d);
       finish();

    }
    void saveDiaryToFirebase(diary d){
        DocumentReference documentReference;


        if(isEditMode){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(d).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DiaryAdd.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(DiaryAdd.this, "Failed while loading", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
   void  deleteDataFromFirebase(){
       DocumentReference documentReference;
           documentReference=Utility.getCollectionReferenceForNotes().document(docId);

       documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   Toast.makeText(DiaryAdd.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                   finish();
               }
               else{
                   Toast.makeText(DiaryAdd.this, "Failed  Deleting", Toast.LENGTH_SHORT).show();

               }
           }
       });

    }




    public void Gretting(){
        TextView greetingTextView = findViewById(R.id.GrettingtextView);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 12) {
            greetingTextView.setText("Good morning!!");
        } else if (hour >= 12 && hour < 17) {
            greetingTextView.setText("Good afternoon!!");
        } else {
            greetingTextView.setText("Good evening!!");
        }
    }

}