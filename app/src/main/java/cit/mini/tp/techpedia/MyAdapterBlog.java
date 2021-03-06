package cit.mini.tp.techpedia;

/**
 * Created by SARADHA on 21-02-2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MyAdapterBlog  extends RecyclerView.Adapter<MyAdapterBlog.ViewHolder>{
    private Context context;
    private List<PostBaseclass> posts;
    private DatabaseReference mDatabase;

    StorageReference storageReference;
    ProgressDialog pd;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conten_blog_activity, parent, false);
       /* storageReference = FirebaseStorage.getInstance().getReference(PostActivity.Constants.Storagefile_upload);*/
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    public MyAdapterBlog(Context context, List<PostBaseclass> posts) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostBaseclass postBaseclass = posts.get(position);

        holder.imagetitle.setText(postBaseclass.getImageTitle());
        holder.imagename.setText(postBaseclass.getImageName());
        holder.description.setText(postBaseclass.getImage_Desription());
        holder.blogId.setText(postBaseclass.getBlogId());
        if(postBaseclass.getCommentCounts()!=null) {
            holder.comment.setText("Comments(" + postBaseclass.getCommentCounts() + ")");
        }else {
            holder.comment.setText("Comments(0)");
        }
        if(postBaseclass.getisapproved().equals("true")){
            holder.approve.setText("Approved");
            holder.approve.setEnabled(false);

        }
//        holder.comment.setText(postBaseclass.getComments());
     /*   holder.filedowname.setText(postBaseclass.getFilename());*/




        Glide.with(context).load(postBaseclass.getImageURL()).into(holder.imageView);
    }



    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView imagename,imagetitle,description,comment,blogId,approve;
       /* FloatingActionButton filedownload;*/
        public ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            imagename = (TextView) itemView.findViewById(R.id.tvimgname);

            imagetitle=(TextView)itemView.findViewById(R.id.tvimgtitle);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            description=(TextView)itemView.findViewById(R.id.tvdescription);
            comment=(TextView)itemView.findViewById(R.id.tvcomment);
            approve=(Button)itemView.findViewById(R.id.btapprove);


            blogId=(TextView)itemView.findViewById(R.id.blogId);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // System.out.println(view.getId());
                    Intent intent = new Intent(view.getContext(), CommentActivity.class);
                    intent.putExtra("postId", blogId.getText());
                    view.getContext().startActivity(intent);

                }
            });
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            database.child("first_name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   String name= dataSnapshot.getValue().toString();
                   if(!name.equalsIgnoreCase("Admin")){
                       approve.setVisibility(View.GONE);
                   }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //hideProgress();
                    Log.d("==>", "==>" + databaseError);

                }
            });


            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDatabase = FirebaseDatabase.getInstance().getReference(PostActivity.Constants.Database_upload).child(blogId.getText().toString()).child("isapproved");//adding an event listener to fetch values
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                          mDatabase.setValue("true");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });

        }

    }



    }

