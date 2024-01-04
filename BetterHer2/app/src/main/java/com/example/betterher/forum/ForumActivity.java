package com.example.swipablecardtest.forum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.swipablecardtest.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 10; // Determines how many posts to load at once
    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> postList;
    private DocumentSnapshot lastVisible; // Last visible document from Firestore query
    private boolean isLoading = false; // Tracks if more data is being loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        recyclerView = findViewById(R.id.masonry_grid);
        int spanCount = 2; // Number of columns in the grid
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        postList = new ArrayList<>();
        loadPosts(); // Assume you have a method to fill postList with data from Firestore

        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MarginItemDecoration(10)); // Replace 15 with your desired margin size in pixels.

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ForumActivity.this, PostActivity.class);
            startActivity(intent);
        });

        // Call this method in onCreate or when initializing the activity
        setupRealtimeUpdates();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int totalItemCount = staggeredLayoutManager.getItemCount();
                    int[] lastVisibleItemPositions = staggeredLayoutManager.findLastVisibleItemPositions(null);
                    int lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions);

                    // If the user is nearing the end of the list, load more posts
                    if (!isLoading && lastVisibleItem + 5 > totalItemCount) {
                        isLoading = true; // Set isLoading to true before loading more posts
                        loadPosts();
                    }
                }
            }

            private int getLastVisibleItem(int[] lastVisibleItemPositions) {
                int maxSize = 0;
                for (int position : lastVisibleItemPositions) {
                    maxSize = Math.max(maxSize, position);
                }
                return maxSize;
            }
        });
    }


    private void loadPosts() {
        if (isLoading) {
            return; // Already loading data
        }
        isLoading = true;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Post").orderBy("createdAt", Query.Direction.DESCENDING).limit(PAGE_SIZE);

        // Use the lastVisible document to paginate and get the next set of posts.
        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();

                if (snapshots != null && !snapshots.isEmpty()) {
                    // Update lastVisible to the last document in the current fetch.
                    lastVisible = snapshots.getDocuments().get(snapshots.size() - 1);

                    // Clear existing data before adding new posts
                    postList.clear();

                    for (DocumentSnapshot document : snapshots) {
                        String title = document.getString("title");
                        String content = document.getString("content");
                        List<String> images = (List<String>) document.get("images");

                        Post post = new Post(title, content, images);
                        postList.add(post);
                    }

                    adapter.notifyDataSetChanged(); // Notify the adapter after adding new posts.
                }

                isLoading = false; // Update isLoading to false after data is loaded.
            } else {
                Toast.makeText(ForumActivity.this, "Error fetching posts: " + task.getException(), Toast.LENGTH_SHORT).show();
                isLoading = false; // Update isLoading to false on error as well.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetPagination(); // Reset parameters when coming back to the activity
        loadPosts(); // Load posts in case there are new updates
    }

    private void resetPagination() {
        lastVisible = null; // Reset the lastVisible document to load from the start
    }

    private void setupRealtimeUpdates() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Post").orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(ForumActivity.this, "Error listening for updates: " + e, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                // Instead of clearing the list, identify and update or add new posts
                                Post post = document.toObject(Post.class);
                                if (post != null) {
                                    updateOrAddPost(post);
                                }
                            }
                            adapter.notifyDataSetChanged(); // Refresh the adapter
                        }
                    }
                });
    }

    private void updateOrAddPost(Post newPost) {
        int existingPostIndex = -1;

        // Loop through the existing posts to find a match
        for (int i = 0; i < postList.size(); i++) {
            Post existingPost = postList.get(i);
            // Assuming the title is unique and can be used as an identifier
            if (existingPost.getTitle().equals(newPost.getTitle())) {
                existingPostIndex = i;
                break;
            }
        }

        if (existingPostIndex >= 0) {
            // Post exists, update it
            Post existingPost = postList.get(existingPostIndex);
            existingPost.setContent(newPost.getContent());
            existingPost.setImageUrls(newPost.getImageUrls()); // Assuming you have a setter for imageUrls
            // Update any other fields of the Post class as needed

            // Update the post in the list
            postList.set(existingPostIndex, existingPost);
        } else {
            // Post doesn't exist, add it to the beginning of the list
            postList.add(0, newPost);
        }

        // Notify the adapter about the data change
        adapter.notifyDataSetChanged();
    }



}
