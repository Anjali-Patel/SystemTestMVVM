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
import com.mvvmmodelrecyclerview.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> implements Filterable {
    private List<Employee> employees;
    private  List<Employee> employeesSearchlist;
    Context application;
    public NoteAdapter(Context application,List<Employee> employees) {
        this.employees = employees;
        this.application = application;
        employeesSearchlist = new ArrayList<>(employees);
    }

//    public NoteAdapter() {
//        employees = new ArrayList<>();
//        employeesSearchlist =new ArrayList<>(employees);
//    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.textViewTitle.setText(employee.getFirstName());
        holder.textViewDescription.setText(employee.getDept_name());

    }
    @Override
    public int getItemCount() {
        return employees.size();
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
private final Filter exampleFilter=new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
   List<Employee> filteredList= new ArrayList<>();
   if(constraint==null||constraint.length()==0){
       filteredList.addAll(employeesSearchlist);
   }else{
       String filterPattern=constraint.toString().toLowerCase().trim();
       for(Employee item:employees){
        if( item.getFirstName().toLowerCase().contains(filterPattern)|| item.getDept_name().toLowerCase().contains(filterPattern)) {
            filteredList.add(item);
        }else{
            Toast.makeText(application,"No data found",Toast.LENGTH_LONG).show();
        }
       }
   }
        FilterResults filterResults= new FilterResults();
        filterResults.values=filteredList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        employees.clear();
        employees.addAll((List) results.values);
        notifyDataSetChanged();

    }
};
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
        }
    }
}
