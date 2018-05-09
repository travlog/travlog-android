package com.travlog.android.apps.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.travlog.android.apps.R;
import com.travlog.android.apps.models.Note;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public interface Delegate {
        void noteViewHolderItemClick(@NonNull NoteViewHolder viewHolder, @NonNull Note note);
    }

    private final Delegate delegate;

    @BindView(R.id.title)
    AppCompatTextView title;

    public NoteViewHolder(final @NonNull View itemView, final @NonNull Delegate delegate) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.delegate = delegate;
    }

    public void bindData(final @NonNull Note item) {
        this.title.setText(item.title);

        itemView.setOnClickListener(view -> this.delegate.noteViewHolderItemClick(this, item));
    }
}
