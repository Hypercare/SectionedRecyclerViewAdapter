package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class Example1Fragment extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex1, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                sectionAdapter.addSection(new ContactsSection(String.valueOf(alphabet), contacts));
            }
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }

    private class ContactsSection extends StatelessSection {

        final String title;
        final List<String> list;

        ContactsSection(String title, List<String> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.section_ex1_item)
                    .headerResourceId(R.layout.section_ex1_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String name = list.get(position);

            itemHolder.tvItem.setText(name);
            itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),
                            String.format("Clicked on position #%s of Section %s",
                                    sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()),
                                    title),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvItem;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = view.findViewById(R.id.imgItem);
            tvItem = view.findViewById(R.id.tvItem);
        }
    }
}
