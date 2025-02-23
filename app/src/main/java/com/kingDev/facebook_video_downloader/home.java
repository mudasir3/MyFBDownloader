package com.kingDev.facebook_video_downloader;

/**
 * Created by mac on 25/01/16.
 */

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dialog.dialoginfo;
import func.reg;

import static android.content.Context.CLIPBOARD_SERVICE;


public class home extends Fragment {

    private String html = "", desc = "", imagina = "", url = "", video = "", videoArray = "";
    EditText textField;
    Button past, btnshow, btnsharethisapp, btnrateus, btnhelp, btnmoreapp;
    TextView textView;
    ProgressDialog prgDialog;
    ArrayList<String> jVideo, jQuality;
    // variable to track event time
    private long mLastClickTime = 0;
    ViewPager viewPager;

    private ClipData myClip;

    String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {

        View rootView = inflater.inflate(R.layout.home, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        LinearLayout linearlayout = (LinearLayout) rootView.findViewById(R.id.unitads);
        config.admob.admobBannerCall(getActivity(), linearlayout);
        final android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                getActivity().getSystemService(CLIPBOARD_SERVICE);
       // myClipboard = (ClipboardManager) home.this.getContext().getSystemService(CLIPBOARD_SERVICE);

        textField = (EditText) rootView.findViewById(R.id.webobo);
        past = (Button) rootView.findViewById(R.id.btndl);
        btnshow = (Button) rootView.findViewById(R.id.btnshow);
        textView = (TextView) rootView.findViewById(R.id.textView);
        btnsharethisapp = (Button) rootView.findViewById(R.id.sharethisapp);
        btnrateus = (Button) rootView.findViewById(R.id.rateus);
        //btnhelp= (Button) rootView.findViewById(R.id.help);
        // btnmoreapp =(Button)rootView.findViewById(R.id.moreapp);

        /*btnhelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), help.class);
                startActivity(intent);
            }
        });*/

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setCancelable(false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String packageName = getActivity().getPackageName();


        myClip = clipboard.getPrimaryClip();

        if (myClip != null) {

            if (myClip.getItemCount() > 0) {
                for (int i = 0; i < myClip.getItemCount(); i++) {
                    String clipBrdTxt = myClip.getItemAt(i).getText().toString();
                    if (clipBrdTxt.contains("facebook.com/")) {
                        textField.setText(func.reg.getBack(clipBrdTxt, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));

                    }
                    break;
                }
            } else {
                Toast.makeText(getActivity(), "Empty clipboard!", Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("url")) {
            textField.setText(func.reg.getBack(intent.getStringExtra("url").toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));

            showContent(textField);
            getActivity().getIntent().removeExtra("url");
        }

        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (!textView.getText().toString().isEmpty()) {
                    showContent(textField);
                }
                return false;
            }
        });

        past.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    if (myClip != null) {

                        if (myClip.getItemCount() > 0) {
                            for (int i = 0; i < myClip.getItemCount(); i++) {
                                String clipBrdTxt = myClip.getItemAt(i).getText().toString();
                                if (clipBrdTxt.contains("facebook.com/")) {
                                    textField.setText(func.reg.getBack(clipBrdTxt, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));
                                }
                                break;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Empty clipboard!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    showContent(textField);
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        });

        btnshow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

                } else {

                    showContent(textField);

                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        });

        btnsharethisapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                func.share.mShareText("Hey my friend check out this app\n https://play.google.com/store/apps/details?id=" + packageName + " \n", getActivity());

            }
        });

        btnrateus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }

            }
        });
        /*
        btnmoreapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:mobicodepro")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:mobicodepro")));
                }

            }
        });
        */

        return rootView;

    }


    private void showContent(EditText textField) {

        url = textField.getText().toString().replaceAll(" ", "");

        if (reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)").isEmpty()) {

            Toast.makeText(getActivity(), "Please Check Url. if correct!", Toast.LENGTH_LONG).show();

        } else {

            video = "";
            imagina = "";
            desc = "";
            videoArray = "";

            // correct url structure
            if (url.contains("facebook")) {

                url = "https://m.facebook." + reg.getBack(url, "(((?!.com).)+$)");

            }


            url = func.reg.getBack(url, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)");

            AsyncHttpClient client = new AsyncHttpClient();
            client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
            client.get(url, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    // Initiated the request
                    prgDialog.setMessage("Loading...");
                    prgDialog.show();

                }

                @Override
                public void onFinish() {
                    // Completed the request (either success or failure)
                    prgDialog.hide();
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                    // TODO Auto-generated method stub
                    prgDialog.hide();
                    Toast.makeText(getActivity(), "Conexion Faild!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                    // TODO Auto-generated method stub

                    html = responseBody;

                    int i = 0;
                    for (Header header : headers) {

                        Log.e("header : ", header.getValue());
                    }


                    if (url.contains("facebook.com") || url.contains("fb.com")) {

                        facebook();

                    }

                }
            });

        }
    }


    private void asynHttp(final String web) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        client.get(web, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                // Initiated the request

                prgDialog.setMessage("getting video...");
                prgDialog.show();

            }

            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
                prgDialog.hide();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                Toast.makeText(getActivity(), "Conexion Faild!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String responseBody) {
                // TODO Auto-generated method stub

                html = responseBody;


                if (web.contains("savedeo.com")) {

                    video = reg.getBack(html, "href=\"(.+?\\.mp4)\"");

                    mDialog();

                } else {

                    String powerJS = reg.getBack(html, "data-config=\"(.+?)\"");

                    if (!func.json.jsonObject(powerJS, "video_url").isEmpty()) {

                        asynHttp("https://savedeo.com/download?url=" + url);

                    } else if (!func.json.jsonObject(powerJS, "vmap_url").isEmpty()) {

                        try {

                            video = reg.getBack(func.httpRequest.get(func.json.jsonObject(powerJS, "vmap_url")), "<MediaFile>[^<]+<\\!\\[CDATA\\[([^\\]]+)");
                            mDialog();

                        } catch (Exception e) {

                        }

                    }

                }

            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();

        Intent a = getActivity().getIntent();

        if (a.hasExtra("url")) {

            textField.setText(func.reg.getBack(a.getStringExtra("url").toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)"));
            a.removeExtra("url");
            showContent(textField);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void facebook() {

        String jsonVideo = escapeXml(reg.getBack(html, "\"([^\"]+)\" data-sigil=\"inlineVideo\""));

        String jsonImage = escapeXml(reg.getBack(html, "data-store=\"([^\"]+imgsrc[^\"]+)\""));

        String jGif = reg.getBack(html, "class=\"_4o54\".+?&amp;url=(.+?)&");

        try {

            if (!jsonVideo.isEmpty()) {
                JSONObject obj = new JSONObject(jsonVideo);
                video = obj.getString("src");

            } else if (!jGif.isEmpty()) {

                imagina = URLDecoder.decode(jGif, "UTF-8");

            } else if (!jsonImage.isEmpty()) {

                JSONObject obj = new JSONObject(jsonImage);
                imagina = obj.getString("imgsrc");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mDialog();

    }


    public String escapeXml(String s) {
        return s.replaceAll("&#123;", "{").replaceAll("&#125;", "}").replaceAll("&amp;", "&").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replaceAll("&apos;", "'");
    }

    public void mDialog() {

        if (!video.isEmpty() || !imagina.isEmpty() || !videoArray.isEmpty()) {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialoginfo info = new dialoginfo();
            Bundle args = new Bundle();
            args.putString("videoArray", videoArray);
            args.putString("video", video);
            args.putString("image", imagina);
            if (desc.length() > 300) {
                desc = desc.substring(0, 300);
            }
            args.putString("desc", desc);
            info.setArguments(args);
            info.show(fm, "fragment_info");

        } else {

            Toast.makeText(getContext(), "There is No results try again with new Link!", Toast.LENGTH_LONG).show();
        }
    }

}