package com.example.habittracker;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HabitItemTouchHelper extends ItemTouchHelper.Callback{

    private final ItemTouchHelperAdapter touchAdapter;

    public HabitItemTouchHelper(ItemTouchHelperAdapter touchAdapter) {
        this.touchAdapter = touchAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * Used to reset the color of the view after it has been dragged to its original color
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white)
        );
    }

    /**
     * Changes the color of the view that is currently in the drag state
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * Sets the movement flags for dragging and swiping views
     * The swipe flags are not implemented as we do not want that functionality
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //Setting swipeflags to 0 will remove swiping functionality
        final int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * When the item is done being moved it calls the onItemMove to do further logic
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        touchAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //Functionality not implemented as its not needed
    }
}
