package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter=new SearchAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=findViewById(R.id.rvList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> items=new ArrayList<>();
        for(int i=0;i<100;i++){
            items.add(String.valueOf(i));
        }
        mSearchAdapter.appendList(items);
        mRecyclerView.setAdapter(mSearchAdapter);
        EditText et = findViewById(R.id.SearchText);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                mSearchAdapter.getFilter().filter(sequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
class TextViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    private TextView mTextView;
    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView=itemView.findViewById(R.id.text);
        itemView.setOnClickListener(this);
        System.out.println("111111111111111");
    }
    public void bind(String text){
        mTextView.setText(text);
    }

    @Override
    public void onClick(View view) {
       Intent intent=new Intent(view.getContext(),MainActivity.class);
       intent.putExtra("extra",mTextView.getText().toString());
       view.getContext().startActivity(intent);
    }
}
/*
class SearchAdapter extends RecyclerView.Adapter<TextViewHolder>{

    @NonNull
    private List<String> mItems=new ArrayList<>();
    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void notifyItems(@NonNull List<String>items){
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}*/
//这里实现Filterable接口
class SearchAdapter extends RecyclerView.Adapter<TextViewHolder> implements Filterable {

    private List<String> mItems = new ArrayList<>();
    private List<String> mFilterItems = new ArrayList<>();

    public void appendList(List<String> list) {
        mItems = list;
        //这里需要初始化filterList
        mFilterItems = list;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.bind(mFilterItems.get(position));
    }

    @Override
    public int getItemCount() {
        //注意这里需要是过滤后的list
        return mFilterItems.size();
    }
    //重写getFilter()方法
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterItems = mItems;
                } else {
                    List<String> filteredList = new ArrayList<>();
                    for (String str : mItems) {
                        //这里根据需求，添加匹配规则
                        if (str.contains(charString)) {
                            filteredList.add(str);
                        }
                    }

                    mFilterItems = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterItems;
                return filterResults;
            }
            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterItems = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}