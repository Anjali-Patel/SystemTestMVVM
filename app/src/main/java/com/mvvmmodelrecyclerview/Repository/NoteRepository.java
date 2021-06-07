package com.mvvmmodelrecyclerview.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mvvmmodelrecyclerview.Global;
import com.mvvmmodelrecyclerview.Interface.NoteDao;
import com.mvvmmodelrecyclerview.RoomDatabase.NoteDatabase;
import com.mvvmmodelrecyclerview.model.Employee;
import com.mvvmmodelrecyclerview.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteRepository {
    private List<Employee> employees = new ArrayList<>();

    private MutableLiveData<List<Employee>> mutableLiveData = new MutableLiveData<>();
    private NoteDao noteDao;
    Application application;
    private LiveData<List<Note>> allNotes;
    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        this.application=application;
    }

//For fetching Offline data
    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    //for fetching Online data
    public MutableLiveData<List<Employee>> getMutableLiveData() {
        employees.clear();
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, Global.API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("DATA");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                Employee employee=new Employee();
                                employee.setDept_name(jsonObject1.getString("dept_name"));
                                employee.setFirstName(jsonObject1.getString("name"));
                                employees.add(employee);
                                mutableLiveData.setValue(employees);
                                //For fetching Offline data
                                Note note=new Note( employee.getFirstName(),  employee.getDept_name());
                                new InsertNoteAsyncTask(noteDao).execute(note);
                            }

                        } catch ( JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(application);
        requestQueue.add(jsonRequest);
        return mutableLiveData;
    }


}
