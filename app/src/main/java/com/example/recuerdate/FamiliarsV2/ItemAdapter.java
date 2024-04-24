package com.example.recuerdate.FamiliarsV2;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    private Context context;

    ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Item item = itemList.get(itemViewHolder.getAdapterPosition());
        itemViewHolder.tvItemTitle.setText(item.getItemTitle());

        itemViewHolder.btnAddSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubItemDialog(itemViewHolder.getAdapterPosition());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        SubItemAdapter subItemAdapter = new SubItemAdapter(item.getSubItemList());

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private Button btnAddSubItem;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            btnAddSubItem = itemView.findViewById(R.id.btn_item);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
        }
    }

    private void showAddSubItemDialog(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_subitem, null);
        dialogBuilder.setView(dialogView);

        EditText editTextSubItemTitle = dialogView.findViewById(R.id.edit_text_subitem_title);
        EditText editTextSubItemDescription = dialogView.findViewById(R.id.edit_text_subitem_description);
        Button buttonAddSubItem = dialogView.findViewById(R.id.button_add_subitem);

        AlertDialog alertDialog = dialogBuilder.create();

        buttonAddSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subItemTitle = editTextSubItemTitle.getText().toString().trim();
                String subItemDescription = editTextSubItemDescription.getText().toString().trim();

                if (!subItemTitle.isEmpty() && !subItemDescription.isEmpty()) {
                    itemList.get(position).getSubItemList().add(new SubItem(subItemTitle, subItemDescription));
                    notifyDataSetChanged(); // Refresh the RecyclerView
                    alertDialog.dismiss(); // Dismiss the dialog
                }
            }
        });

        alertDialog.show();
    }
}

