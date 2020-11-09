package com.jeasonlyx.myhealth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Fragment_Search extends Fragment {

    private FragmentActivity host_Activity;

    private WebView webview;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageButton delete_button;
    private ImageButton search_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        host_Activity = requireActivity();

        webview = view.findViewById(R.id.fragment_search_webView);
        autoCompleteTextView = view.findViewById(R.id.fragment_search_text);
        delete_button = view.findViewById(R.id.fragment_search_delete);
        search_button = view.findViewById(R.id.fragment_search_search);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient()); // make sure not use system browser

        webview.loadUrl("https://www.goodrx.com");

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToWebsite(v);
            }
        });

        String[] urls = {"www.google.com", "www.goodrx.com"};
        ArrayAdapter url_adapter = new ArrayAdapter(host_Activity, android.R.layout.select_dialog_item, urls);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(url_adapter);

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    turnToWebsite(v);
                }
                return false;
            }
        });

        return view;
    }

    public void turnToWebsite(View v){
        String prefix = "https://";
        String url = autoCompleteTextView.getText().toString().trim();
        if(!url.startsWith(prefix)) url = prefix + url;
        webview.loadUrl(url);

        InputMethodManager inputMethodManager =
                (InputMethodManager)host_Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
