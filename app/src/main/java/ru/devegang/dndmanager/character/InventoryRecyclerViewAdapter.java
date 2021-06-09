package ru.devegang.dndmanager.character;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.devegang.dndmanager.R;
import ru.devegang.dndmanager.dialogs.ItemDialog;
import ru.devegang.dndmanager.dialogs.RollResultDialog;
import ru.devegang.dndmanager.entities.Item;
import ru.devegang.dndmanager.entities.Spell;
import ru.devegang.dndmanager.entities.rolls.RollingFormula;
import ru.devegang.dndmanager.entities.rolls.RollingResult;
import ru.devegang.dndmanager.networking.NetworkService;

public class InventoryRecyclerViewAdapter extends RecyclerView.Adapter<InventoryViewHolder> {

    List<Item> items;
    List<Item> favorites;
    private LayoutInflater layoutInflater;
    private WeakReference<InventoryList> inventoryListWeakReference;

    boolean favFlag = false;

    public InventoryRecyclerViewAdapter(InventoryList inventoryList, List<Item> items) {
        this.items = items;
        inventoryListWeakReference = new WeakReference<>(inventoryList);
        this.layoutInflater = LayoutInflater.from(inventoryList.getContext());
    }

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
        if(favFlag && favorites == null) {
            extractFavorites();
        }
    }

    public void extractFavorites() {
        favorites = new LinkedList<>();
        for (Item item : items) {
            if(item.getFavorite()) {
                favorites.add(item);
            }
        }
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.card_item,parent, false);
         item.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 handleRecyclerItemClick((RecyclerView) parent, view);
             }
         });

        return new InventoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {

        List<Item> items;
        if(favFlag) {
            items = favorites;
        } else {
            items = this.items;
        }
        Item item = items.get(position);

        holder.name.setText(item.getName());
        holder.description.setText(item.getDefinition());
        holder.formula.setVisibility(View.GONE);

        if(item.getFavorite()) {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star);
        } else {
            holder.favButton.setImageResource(R.drawable.ic_favorite_star_outline);
        }

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setFavorite(!item.getFavorite());
                NetworkService.getInstance()
                        .getRestCharacterAPIv2()
                        .setFavoriteItem(item.getId(),item.getFavorite())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                notifyItemChanged(position);
                                if(favorites != null) {
                                    if(item.getFavorite()) {
                                        favorites.add(item);
                                    } else {
                                        favorites.removeIf(x -> x.getId() == item.getId());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(layoutInflater.getContext(),holder.card);
                popupMenu.inflate(R.menu.edit_delete_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.basicEditItem:
                                InventoryList inventoryList = inventoryListWeakReference.get();
                                if(inventoryList != null) {
                                    FragmentManager fragmentManager = inventoryList.getParentFragmentManager();
                                    ItemDialog itemDialog = new ItemDialog();
                                    itemDialog.setCancelable(true);
                                    itemDialog.show(fragmentManager, String.valueOf(item.getId()));
                                }
                                break;
                            case R.id.basicDeleteItem:
                                NetworkService.getInstance()
                                        .getRestCharacterAPIv2()
                                        .deleteItem(item.getId())
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if(response.isSuccessful()) {
                                                    items.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {

                                            }
                                        });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        if(!item.getFormula().isEmpty()) {
            holder.formula.setVisibility(View.VISIBLE);
            RollingFormula rollingFormula = RollingFormula.gerRollingFormula(item.getFormula());
            holder.formula.setText(rollingFormula.getReadable());
        }
        holder.weight.setText(String.valueOf(item.getWeight()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Item item;
        if(favFlag) {
            item = this.favorites.get(itemPosition);
        } else {
            item = this.items.get(itemPosition);
        }

        if(!item.getFormula().isEmpty()) {
            NetworkService.getInstance()
                    .getRestRollingAPIv2()
                    .simpleRoll(item.getFormula())
                    .enqueue(new Callback<RollingResult>() {
                        @Override
                        public void onResponse(Call<RollingResult> call, Response<RollingResult> response) {
                            if(response.isSuccessful()) {
                                InventoryList inventoryList = inventoryListWeakReference.get();
                                if(inventoryList != null) {
                                    FragmentManager fragmentManager = inventoryList.getParentFragmentManager();
                                    RollResultDialog saveRollResultDialog = new RollResultDialog();
                                    saveRollResultDialog.setCancelable(true);
                                    saveRollResultDialog.show(fragmentManager,String.valueOf(response.body().getResult()));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RollingResult> call, Throwable t) {

                        }
                    });
        }



    }

}
