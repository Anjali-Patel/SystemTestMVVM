package com.mvvmmodelrecyclerview;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.mvvmmodelrecyclerview.adapter.NoteAdapter;
import com.mvvmmodelrecyclerview.adapter.RoomDatabaseAdapter;
import com.mvvmmodelrecyclerview.model.Employee;
import com.mvvmmodelrecyclerview.model.Note;
import com.mvvmmodelrecyclerview.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    NoteAdapter adapter;
    RoomDatabaseAdapter roomAdapter;
    List<Employee> employeesList;
    List<Note> RoomdataList;
    Application application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        if(Global.isInternetAvailable(MainActivity.this)){
        employeesList=new ArrayList<>();
    adapter = new NoteAdapter(this,employeesList);
    recyclerView.setAdapter(adapter);
      getAllEmployee();
    }else{
     Toast.makeText(MainActivity.this,"No Internet Connection Available.Please check your internet connection",Toast.LENGTH_LONG).show();
            RoomdataList=new ArrayList<>();
     roomAdapter = new RoomDatabaseAdapter(this,RoomdataList);
    recyclerView.setAdapter(roomAdapter);
    noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
        @Override
        public void onChanged(@Nullable List<Note> notes) {
            roomAdapter.setNotes(notes);
        }
    });

}
    }
    private void getAllEmployee() {
        noteViewModel.getAllEmployee().observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(@Nullable List<Employee> employees) {
                adapter.setEmployees((List<Employee>) employees);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem searchitem= menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(Global.isInternetAvailable(MainActivity.this)){
                    adapter.getFilter().filter(newText);
                }else{
                    roomAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
      return  true;
    }
}
