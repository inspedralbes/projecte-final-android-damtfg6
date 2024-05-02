package com.example.recuerdate.FamiliarsV2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    private Context context;

    private Uri selectedImageUri;

    // ImageView reference moved here
    public ImageView imageViewSelectedImage;

    public ImageView imageViewSelectedImage2;

    public static final int REQUEST_IMAGE = 1001;

    private Fragment fragment;

    ItemAdapter(Context context, List<Item> itemList, Fragment fragment) {
        this.context = context;
        this.itemList = itemList;
        this.fragment = fragment;
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
        private ImageButton btnAddSubItem;
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
        imageViewSelectedImage = dialogView.findViewById(R.id.image_view_sub_item); // Add ImageView
        Button btnSeleccionarFoto = dialogView.findViewById(R.id.button_select_image);
        AlertDialog alertDialog = dialogBuilder.create();

        // Handle image selection from the gallery
        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fragment.startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        buttonAddSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subItemTitle = editTextSubItemTitle.getText().toString().trim();
                String subItemDescription = editTextSubItemDescription.getText().toString().trim();
                ImageView etImagen = imageViewSelectedImage2;


                if (!subItemTitle.isEmpty() && !subItemDescription.isEmpty()) {
                    SubItem newSubItem = new SubItem(subItemTitle, subItemDescription, etImagen);
                    itemList.get(position).getSubItemList().add(newSubItem);
                    notifyDataSetChanged(); // Refresh the RecyclerView
                    alertDialog.dismiss(); // Dismiss the dialog
                    editTextSubItemTitle.setText("");
                    editTextSubItemDescription.setText("");
                }
            }
        });

        alertDialog.show();
    }

    public void setSelectedImageUri(Uri selectedImageUri) {
        this.selectedImageUri = selectedImageUri;
            imageViewSelectedImage.setImageURI(selectedImageUri);
            imageViewSelectedImage2 = imageViewSelectedImage;
    }
}

