package com.jsi.mbrana.Workflow.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsi.mbrana.Models.MenuObject;
import com.jsi.mbrana.R;

import java.util.List;

/**
 * Created by Sololia on 6/13/2016.
 */
public class mainMenuAdapter extends RecyclerView.Adapter<mainMenuAdapter.MyViewHolder> {
    private static MenuOnClickListener myClickListener;
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<MenuObject> menuList;

    public mainMenuAdapter(List<MenuObject> menuList) {
        this.menuList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MenuObject menu = menuList.get(position);
        holder.textViewMenuTitle.setText(menu.getTitle());
        holder.textViewMenuDesc.setText(menu.getDescription());

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setOnItemClickListener(MenuOnClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MenuOnClickListener {
        public void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewMenuTitle, textViewMenuDesc;

        public MyViewHolder(View view) {
            super(view);
            textViewMenuTitle = (TextView) view.findViewById(R.id.textViewMenuTitle);
            textViewMenuDesc = (TextView) view.findViewById(R.id.textViewMenuDesc);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}