package com.cristichi.secuencia2;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cristichi.secuencia2.data.UploadCustomGamemode;

import java.util.ArrayList;
import java.util.List;

public class CustomEditorActivity extends ActivityWithMusic {

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_editor);

        final EditText etName = findViewById(R.id.etName);

        RecyclerView rv = findViewById(R.id.rvCustomValues);
        adapter = new RecyclerAdapter(new ArrayList<String>());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnAdd = findViewById(R.id.btnAddValue);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playRecordedCluk();
                new CustomDialogAsk(CustomEditorActivity.this, adapter).show();
            }
        });

        ImageButton btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundSystem.playCartoonSlipUp();
                String title = etName.getText().toString().trim();
                if (title.isEmpty()){
                    new AlertDialog.Builder(CustomEditorActivity.this).setMessage(R.string.custom_editor_error_empty_name).show();
                } else if (adapter.getStringList().size()<2){
                    new AlertDialog.Builder(CustomEditorActivity.this).setMessage(R.string.custom_editor_error_not_enough_values).show();
                }else {
                    String values = adapter.toString();
                    UploadCustomGamemode task = new UploadCustomGamemode(CustomEditorActivity.this, title, values);
                    task.execute();
                }
            }
        });
    }

    /**
     * Diálogo que muestra un campo de texto y un botón de añadir que añade un elemento a la lista
     */
    private class CustomDialogAsk extends Dialog{
        private RecyclerAdapter adapter;
        public CustomDialogAsk(Activity a, RecyclerAdapter adapter) {
            super(a);
            this.adapter = adapter;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_add_string);

            final EditText et = findViewById(R.id.etValue);

            Button btnCancel = findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });

            Button btnOk = findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundSystem.playRecordedPop();
                    String str = et.getText().toString().trim();
                    if (str.isEmpty()){
                        return;
                    }
                    if (adapter.addItem(str)){
                        CustomDialogAsk.this.dismiss();
                    }
                }
            });

            et.requestFocus();
        }
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvValue;
        ImageButton btnRemove;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tvValue);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }


    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
        private List<String> stringList;

        RecyclerAdapter(List<String> gamemodes){
            this.stringList = gamemodes;
        }

        public void setValues(List<String> gamemodes){
            this.stringList = gamemodes;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom_value, viewGroup, false);
            return new RecyclerViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
            holder.tvValue.setText(stringList.get(position));
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public boolean addItem(String string){
            string = string.trim();
            if (stringList.contains(string)){
                return false;
            }
            stringList.add(string);
            notifyDataSetChanged();
            return true;
        }

        public boolean removeItem(int index){
            try{
                stringList.remove(index);
                notifyDataSetChanged();
                return true;
            }catch (IndexOutOfBoundsException e){
                return false;
            }
        }

        public List<String> getStringList() {
            return stringList;
        }

        @NonNull
        @Override
        public String toString() {
            String sol = "";
            for (String string : stringList){
                sol+=string+"\n";
            }
            return sol.trim();
        }
    }
}
