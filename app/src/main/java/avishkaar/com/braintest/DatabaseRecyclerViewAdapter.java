package avishkaar.com.braintest;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DatabaseRecyclerViewAdapter extends RecyclerView.Adapter<DatabaseRecyclerViewAdapter.DatabaseViewHolder>{
    List<ImageDataForDatabase> data;

    public DatabaseRecyclerViewAdapter(List<ImageDataForDatabase> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DatabaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DatabaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatabaseViewHolder holder, int position) {
        holder.imageView.setImageURI(Uri.parse(data.get(position).iconUri));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DatabaseViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public DatabaseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);
        }
    }
}
