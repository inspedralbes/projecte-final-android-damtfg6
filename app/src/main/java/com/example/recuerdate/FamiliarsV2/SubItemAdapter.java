package com.example.recuerdate.FamiliarsV2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recuerdate.R;
import com.example.recuerdate.Settings;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.gson.Gson;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private List<SubItem> subItemList;
    private Context context;
    private ItemAdapter itemAdapter; // Referencia al adaptador principal para notificar cambios
    private OkHttpClient client;
    private Gson gson;

    private PreferenceManager preferenceManager;

    SubItemAdapter(List<SubItem> subItemList, ItemAdapter itemAdapter, Context context) {
        this.subItemList = subItemList;
        this.itemAdapter = itemAdapter;
        this.context = context;
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        SubItem subItem = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(subItem.getSubItemTitle());
        subItemViewHolder.tvSubItemDescription.setText(subItem.getSubItemDesc());
        Bitmap bitmap = subItem.getSubItemImage(context);
        if (bitmap != null) {
            subItemViewHolder.ivSubItemImage.setImageBitmap(bitmap);
        }

        // Añadir el evento de clic para eliminar el subitem
        subItemViewHolder.ivDeleteSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = subItemViewHolder.getAdapterPosition();
                SubItem itemToRemove = subItemList.get(position);

                // Eliminar el subitem y notificar al adaptador principal
                subItemList.remove(position);
                notifyItemRemoved(position);
                itemAdapter.notifyDataSetChanged();

                // Enviar la petición de eliminación al servidor
                deleteSubItemFromServer(itemToRemove);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        TextView tvSubItemDescription;
        ImageView ivSubItemImage;
        ImageView ivDeleteSubItem; // ImageView para eliminar el subitem

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            tvSubItemDescription = itemView.findViewById(R.id.tv_sub_item_description);
            ivSubItemImage = itemView.findViewById(R.id.img_sub_item);
            ivDeleteSubItem = itemView.findViewById(R.id.img_delete_sub_item);
        }
    }

    private void deleteSubItemFromServer(SubItem subItem) {
        // Obtener el DNI del usuario
        String role = preferenceManager.getString(Constants.KEY_ROLE);
        String dniUsuario = preferenceManager.getString(Constants.KEY_EMAIL);
        if (role.equals("Tutor")) {
            dniUsuario = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_DNI);
        } else if (role.equals("Usuari")) {
            dniUsuario = preferenceManager.getString(Constants.KEY_EMAIL);
        }

        // Crear un mapa para almacenar los datos del subitem a eliminar
        Map<String, Object> subItemMap = new LinkedHashMap<>();
        subItemMap.put("dni", dniUsuario);
        subItemMap.put("subItemTitle", subItem.getSubItemTitle());

        String json = gson.toJson(subItemMap);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Settings.SERVER + ":" + Settings.PORT + "/deleteSubItem")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // Haz algo con la respuesta si es necesario
                }
            }
        });
    }
}

