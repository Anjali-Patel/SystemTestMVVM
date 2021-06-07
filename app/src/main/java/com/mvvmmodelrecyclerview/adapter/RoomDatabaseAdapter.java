package com.mvvmmodelrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.mvvmmodelrecyclerview.R;
import com.mvvmmodelrecyclerview.model.Note;

import java.util.ArrayList;
import java.util.List;

public class RoomDatabaseAdapter extends RecyclerView.Adapter<RoomDatabaseAdapter.NoteHolder> implements Filterable {
        private List<Note> notes;
    private List<Note> notesSearchList;
Context context;
    public RoomDatabaseAdapter(Context context,List<Note> notes) {
        this.notes = notes;
        this.context = context;
        notesSearchList = new ArrayList<>(notes);
    }
    @NonNull
    @Override
    public RoomDatabaseAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new RoomDatabaseAdapter.NoteHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RoomDatabaseAdapter.NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
    }
    @Override
    public int getItemCount() {
        return notes.size();

    }
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }



    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList= new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(notesSearchList);
            }else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(Note item:notes){
                    if( item.getTitle().toLowerCase().contains(filterPattern)|| item.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }else{
                        Toast.makeText(context,"No data found",Toast.LENGTH_LONG).show();

                    }
                }
            }
            FilterResults filterResults= new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

}

