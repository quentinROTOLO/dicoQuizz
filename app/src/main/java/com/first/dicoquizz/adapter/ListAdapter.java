package com.first.dicoquizz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.first.dicoquizz.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ListAdapter extends BaseAdapter {

    private ArrayList<Map<String, Object>> finalList;
    private LayoutInflater m_inflater;
    private Context context;
    private ListView listView;

    //construteur avec notre liste en paramètre
   public ListAdapter(Context context, ArrayList<Map<String, Object>> finalList) {
        this.context = context;
        this.finalList = finalList;
       Log.d("ListAdapter", "dans le constructeur de ListAdapter");
   }

    @Override
    public int getCount() {
        return finalList.size();
    }

    @Override
    public Object getItem(int i) {
        return finalList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i; // item à la position i
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout layoutItem;
        if (view == null) {
           layoutItem = (LinearLayout) m_inflater.inflate(R.layout.activity_word_list , viewGroup, false);
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        } else {
            layoutItem = (LinearLayout) view;
        }
        // récupération de listView de notre layout
        TextView tv_language_reference = (TextView) layoutItem.findViewById(R.id.textView_reference_lang);
        TextView tv_word_reference = (TextView) layoutItem.findViewById(R.id.textView_reference_word);
        TextView tv_word_synonyms_reference = (TextView) layoutItem.findViewById(R.id.textView_reference_word_synonyms);
        TextView tv_language_translated = (TextView) layoutItem.findViewById(R.id.textView_translated_lang);
        TextView tv_word_translated = (TextView) layoutItem.findViewById(R.id.textView_translated_word);
        TextView tv_word_synonyms_translated = (TextView) layoutItem.findViewById(R.id.textView_translated_word_synonyms);
        // Renseignement des valeurs
        tv_language_reference.setText("Langue de référence: " + " " + finalList.get(i).toString());
        tv_word_reference.setText("Mot de référence: " + " " + finalList.get(i).get("referenceWord"));
        tv_word_synonyms_reference.setText("Synonymes du mot: " + " " + finalList.get(i).get("referenceWordSynonyms"));
        tv_language_translated.setText("Langue de àtraduction: " + " " + finalList.get(i).get("translationLanguage"));
        tv_word_translated.setText("Mot traduit: " + " " + finalList.get(i).get("translationWord"));
        tv_word_synonyms_translated.setText("Mot traduit: " + " " + finalList.get(i).get("translationWordSynonyms"));
        return layoutItem;
    }
}
