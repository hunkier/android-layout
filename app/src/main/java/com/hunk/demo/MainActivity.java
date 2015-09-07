package com.hunk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private Context context;
    private Item[] datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        addListener();
        initData();
        setupView();
    }

    private void findView(){
        context = this;
        listView = (ListView) findViewById(R.id.listView);
    }

    private void addListener(){
        listView.setOnItemClickListener(this);
    }

    private void initData(){
        datas = new Item[]{
                new Item(R.string.title_activity_linear_layout,LinearLayoutActivity.class),
                new Item(R.string.title_activity_relative_layout,RelativeLayoutActivity.class),
                new Item(R.string.title_activity_absolute_layout,AbsoluteLayoutActivity.class),
                new Item(R.string.title_activity_table_layout,TableLayoutActivity.class),
                new Item(R.string.title_activity_frame_layout,TableLayoutActivity.class)
        };
    }

    private void setupView(){
        MyAdapter adapter = new MyAdapter(datas);
        listView.setAdapter(adapter);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(context,datas[position].clazz));
    }

    class MyAdapter extends BaseAdapter{
        private Item[] datas;

        public MyAdapter(Item[] datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public Object getItem(int position) {
            return datas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null ;
            if(null== convertView){
                tv = new TextView(context);
                tv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 200));
                tv.setGravity(Gravity.CENTER);
            }else{
                tv = (TextView)convertView;
            }
            tv.setText(datas[position].name);
            return tv;
        }
    }


    class Item{
        int name;
        Class<? extends Activity> clazz;

        public Item(int name, Class<? extends Activity> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }


}



