package in.msmartpay.agent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.msmartpay.agent.db.AppDao;
import in.msmartpay.agent.db.Credentials;
import in.msmartpay.agent.db.DatabaseClient;
import in.msmartpay.agent.location.GPSTrackerPresenter;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;
import in.msmartpay.agent.utility.Util;

public class LoginActivity extends BaseActivity implements View.OnClickListener, GPSTrackerPresenter.LocationListener {
    private Context context;
    private Button btn_login;
    private EditText email, password;
    private CheckBox checkBox;
    private TextView tv, sign_up;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String loginDetails = null, loginCredentials = null;
    private String Email, Password;
    private ProgressDialog pd;
    private String agent_id = "agent_id";
    private String agent_id_full = "agent_id_full", appText = "";
    private String login_url = HttpURL.LOGIN_URL;
    private String forget_url = HttpURL.FORGET_PASSWORD;
    private String signup_url = HttpURL.NEW_MOBILE_REGISTER;
    private EditText edit_verify_mobile = null;
    private boolean flag = false;
    private AppDao appDatabase;
    private GPSTrackerPresenter gpsTrackerPresenter = null;
    private boolean isTxnClick = false;
    private boolean isLocationGet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        setTitle("Login");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        context = LoginActivity.this;
        gpsTrackerPresenter = new GPSTrackerPresenter(this, this, GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);

        sharedPreferences = Util.getMyPref(getApplicationContext());
        editor = sharedPreferences.edit();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        sign_up = findViewById(R.id.sign_up);
        btn_login = findViewById(R.id.btn_login);
        tv = findViewById(R.id.tv);

        btn_login.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        tv.setOnClickListener(this);
        // Save File Data and User Details
        new GetCredentialTask().execute();
    }


    //*******************onClick*************************
    @Override
    public void onClick(View view) {
        if (isConnectionAvailable()) {
            if (view.getId() == R.id.tv) {
                forgetPasswordDialog();
            } else if (view.getId() == R.id.btn_login) {
                Email = email.getText().toString().trim();
                Password = password.getText().toString().trim();
                if (TextUtils.isEmpty(email.getText().toString().trim()) && email.getText().toString().trim().length() < 10) {
                    Toast.makeText(context, "Please enter correct userid !!!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString().trim()) && password.getText().toString().trim().length() < 6) {
                    Toast.makeText(context, "Please enter correct password and password > 6 !!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBox.isChecked()) {
                        new SaveCredentialsTask().execute();
                    }
                    /*if (Util.isPowerSaveMode(context)){
                        loginRequest();
                    }else {*/
                    if (isLocationGet) {
                        Log.e("Location","Y Location");
                        loginRequest();
                    } else {
                        Log.e("Location","N Location");
                        if (!isTxnClick) {
                            isTxnClick = true;
                            gpsTrackerPresenter.checkGpsOnOrNot(GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE);
                        }
                    }
                    //}
                }
            } else if (view.getId() == R.id.sign_up) {
                signUpVerificationDialog();
            }
        } else {
            Toast.makeText(context, "No Internet Connection !!!", Toast.LENGTH_SHORT).show();
        }
    }

    //============Forget Password===============================

    private void forgetPasswordDialog() {
        final Dialog dialog_status = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.signup_mobile_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edit_verify_mobile = dialog_status.findViewById(R.id.edit_verify_mobile);
        TextView title = (TextView) dialog_status.findViewById(R.id.title);
        Button btn_submit = dialog_status.findViewById(R.id.btn_verify);
        Button close_mobile = dialog_status.findViewById(R.id.close_mobile);

        edit_verify_mobile.setText("");
        title.setText("Forget Password");
        btn_submit.setText("Submit");

        btn_submit.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edit_verify_mobile.getText().toString().trim())) {
                edit_verify_mobile.requestFocus();
                Toast.makeText(context, "Please enter mobile number first !!!", Toast.LENGTH_SHORT).show();
            } else if (edit_verify_mobile.getText().toString().trim().length() < 9) {
                edit_verify_mobile.requestFocus();
                Toast.makeText(context, "Mobile number should be 10 digits !!!", Toast.LENGTH_SHORT).show();
            } else {
                forgetPasswordRequest();
                //forgetPasswordDialog("",1);
                dialog_status.dismiss();
            }
        });

        close_mobile.setOnClickListener(view -> {
            dialog_status.cancel();
            hideKeyBoard(edit_verify_mobile);
        });

        dialog_status.show();
    }

    private void forgetPasswordRequest() {
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(true);
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("mob", edit_verify_mobile.getText().toString());

            L.m2("url-forget", forget_url);
            L.m2("Request--forget", jsonObjectReq.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, forget_url, jsonObjectReq,
                    data -> {
                        try {
                            pd.dismiss();
                            L.m2("data-forget", data.toString());
                            if (data.getString("response-code") != null && data.getString("response-code").equals("0")) {
                                forgetPasswordDialog(data.getString("response-message"), Integer.parseInt(data.getString("response-code")));
                            } else {
                                forgetPasswordDialog(data.getString("response-message"), Integer.parseInt(data.getString("response-code")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                pd.dismiss();
                Toast.makeText(context, " " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void forgetPasswordDialog(String msg, int i) {
        final Dialog dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);

        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
        Button trans_status = dialog_status.findViewById(R.id.trans_status_button);

        text.setText(msg);

        if (i == 0) {
            statusImage.setImageResource(R.drawable.trnsuccess);
            trans_status.setOnClickListener(view ->
                    dialog_status.dismiss());
        } else {
            statusImage.setImageResource(R.drawable.failed);
            trans_status.setOnClickListener(view ->
                    dialog_status.dismiss());
        }
        dialog_status.show();
    }
    //==========================================================

    private void loginRequest() {
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("mobileNo", Email)
                    .put("password", Password)
                    .put("latitude", Util.LoadPrefData(getApplicationContext(), Keys.LATITUDE))
                    .put("longitude", Util.LoadPrefData(getApplicationContext(), Keys.LONGITUDE))
                    .put("ip", Util.getIpAddress(context))
                    .put("power_mode", Util.LoadPrefBoolean(context, Keys.POWER_MODE))
                    .put("version", "7.0");
            L.m2("url-login", login_url);
            L.m2("Request--login", jsonObjectReq.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, login_url, jsonObjectReq,
                    data -> {
                        pd.dismiss();
                        try {
                            L.m2("data-login", data.toString());
                            /* if (data.get("Status") != null && data.get("Status").equals("0")) {*/
                            if (data.get("response-code") != null && data.get("response-code").equals("0")) {

                                editor.putString(Keys.USER_CREDENTIALS, "&agent_Id=" + data.get("agent-id") + "&agent_initial=" + data.get("agent-initial") + "&agent_dst_id=" + data.get("agent-dist-id") + "&txn_key=" + data.get("txn_key"));
                                L.m2("check--7>", "&agent_Id=" + data.get("agent-id") + "&agent_initial=" + data.get("agent-initial") + "&agent_dst_id=" + data.get("agent-dist-id") + "&txn_key=" + data.get("txn_key"));
                                editor.putString("agent_id_u", data.get("agent-id") + "");
                                editor.putString("agent_id", "&agent_id=" + data.get("agent-id"));
                                editor.putString(Keys.AGENT_FULL, (String) data.get("agent-initial") + data.get("agent-id"));
                                editor.putString(Keys.BALANCE, "" + data.get("balance"));
                                editor.putString(Keys.TXN_KEY, "" + data.get("txn_key"));
                                editor.putString(Keys.AGENT_ID, data.getString("agent-id"));
                                editor.putString("statement_download", "true");
                                editor.putString(Keys.AGENT_NAME, "" + data.get("agentName"));
                                editor.putString(Keys.AGENT_EMAIL, "" + data.get("emailId"));
                                editor.putString(Keys.AGENT_MOB, "" + data.get("mobile-number"));

                                editor.putString(Keys.SUPPORT_1, "" + data.get("support1"));
                                editor.putString(Keys.SUPPORT_2, "" + data.get("support2"));
                                editor.putString(Keys.SUPPORT_EMAIL, "" + data.get("supportEmail"));
                                editor.putString(Keys.APP_TEXT, "" + data.get(Keys.APP_TEXT));
                                editor.putString(Keys.DMR, "" + data.get("DMR"));
                                editor.putString(Keys.DMR_URL, "" + data.get("DMRUrl"));
                                editor.putString(Keys.DMR_VENDOR, "" + data.get("dmrVendor"));

                                editor.commit();

                                if (data.get(Keys.APP_TEXT) != null) {
                                    appText = data.get(Keys.APP_TEXT).toString().trim();
                                } else {
                                    appText = "";
                                }
                                if (!appText.equalsIgnoreCase("") && appText.length() > 0) {

                                    Log.v("appText", appText);

                                    final Dialog dialog_status = new Dialog(context);
                                    dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog_status.setContentView(R.layout.alert);
                                    dialog_status.setCancelable(true);
                                    ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
                                    statusImage.setImageResource(R.drawable.about);
                                    TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);
                                    text.setText(appText);

                                    final Button trans_status = dialog_status.findViewById(R.id.trans_status_button);
                                    trans_status.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog_status.dismiss();
                                            Intent intent = new Intent();
                                            intent.setClass(context, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    dialog_status.show();
                                } else {
                                    Intent intent = new Intent();
                                    intent.setClass(context, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if (data.get("response-code") != null && data.get("response-code").equals("1")) {
                                Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                            } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                                Toast.makeText(context, (String) data.get("response-message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Wrong UserName or Password! ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            L.m2("data failure", e.toString());
                        }
                    }, error -> {
                pd.dismiss();
                Log.e("Volley : ", error.toString());
                Toast.makeText(context, "Volley " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            pd.dismiss();
            L.m2("data failuer", e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //===============Sign Up Mobile Verification====================
    private void signUpVerificationDialog() {
        final Dialog dialog_status = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialog_status.setCancelable(false);
        dialog_status.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_status.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog_status.setContentView(R.layout.signup_mobile_dialog);
        dialog_status.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edit_verify_mobile = dialog_status.findViewById(R.id.edit_verify_mobile);
        Button btn_verify = dialog_status.findViewById(R.id.btn_verify);
        Button close_mobile = dialog_status.findViewById(R.id.close_mobile);
        edit_verify_mobile.setText("");

        btn_verify.setOnClickListener(view -> {
            if (isConnectionAvailable()) {
                if (TextUtils.isEmpty(edit_verify_mobile.getText().toString())) {
                    edit_verify_mobile.requestFocus();
                    Toast.makeText(context, "Please enter mobile number first !!!", Toast.LENGTH_SHORT).show();
                } else if (edit_verify_mobile.getText().toString().length() < 9) {
                    edit_verify_mobile.requestFocus();
                    Toast.makeText(context, "Mobile number should be 10 digits !!!", Toast.LENGTH_SHORT).show();
                } else {
                    signUpVerifyRequest();
                }
            }
        });

        close_mobile.setOnClickListener(view -> {
            dialog_status.dismiss();
            hideKeyBoard(edit_verify_mobile);
        });

        dialog_status.show();
    }

    private void signUpVerifyRequest() {
        final Dialog dialog_status = new Dialog(context);
        pd = new ProgressDialog(context);
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        try {
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, signup_url,
                    new JSONObject().put("mobile", edit_verify_mobile.getText().toString())
                            .put("version", "5.0"), data -> {
                try {
                    pd.dismiss();
                    L.m2("url-->", signup_url);
                    L.m2("data receive", data.toString());
                    try {
                        if (data.get("response-code") != null && data.get("response-code").equals("0")) {
                            dialog_status.dismiss();
                            flag = true;
                            showDiallog(data);
                        } else if (data.get("response-code") != null && data.get("response-code").equals("2")) {
                            dialog_status.dismiss();
                            showDiallog(data);
                            flag = false;
                        } else if (data.get("response-code") != null && data.get("response-code").equals("1")) {
                            showDiallog(data);
                            dialog_status.dismiss();
                            flag = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Toast.makeText(context, "data failuer " + e.toString(), Toast.LENGTH_SHORT).show();
                    L.m2("data failuer", e.toString());
                }
            }, error -> {
                pd.dismiss();
                Log.e("Volley : ", error.toString());
                Toast.makeText(context, "Volley " + error.toString(), Toast.LENGTH_SHORT).show();
            });
            getSocketTimeOut(objectRequest);
            Mysingleton.getInstance(context).addToRequsetque(objectRequest);
        } catch (Exception e) {
            pd.dismiss();
            L.m2("data failuer", e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void showDiallog(final JSONObject data) {
        final Dialog dialog_status = new Dialog(context);
        dialog_status.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_status.setContentView(R.layout.alert);
        dialog_status.setCancelable(true);
        ImageView statusImage = (ImageView) dialog_status.findViewById(R.id.statusImage);
        statusImage.setImageResource(R.drawable.about);
        TextView text = (TextView) dialog_status.findViewById(R.id.TextView01);

        try {
            text.setText((String) data.get("response-message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Button trans_status = dialog_status.findViewById(R.id.trans_status_button);
        trans_status.setOnClickListener(v -> {

            if (flag == false) {
                dialog_status.dismiss();
            } else {
                dialog_status.dismiss();
                Intent intent = new Intent();
                intent.putExtra("mob", edit_verify_mobile.getText().toString());
                intent.setClass(context, OTPRegisterActivity.class);
                startActivity(intent);
                finish();
            }

        });

        dialog_status.show();
    }
    //==============================================================

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    class SaveCredentialsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            List<Credentials> credentialsList = appDatabase.getAll();
            if (credentialsList != null && credentialsList.size() > 0) {
                appDatabase.delete();
            }
            //creating a task
            Credentials credentials = new Credentials();
            credentials.setUser(Email);
            credentials.setPassword(Password);
            //adding to database
            appDatabase.insert(credentials);
            return null;
        }
    }

    class GetCredentialTask extends AsyncTask<Object, Object, Object> {
        String user = "", pass = "";
        boolean flag = false;

        @Override
        protected Object doInBackground(Object... objects) {
            appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .appDBDao();
            List<Credentials> credentialsList = appDatabase.getAll();
            if (credentialsList != null && credentialsList.size() > 0) {
                user = credentialsList.get(0).getUser();
                pass = credentialsList.get(0).getPassword();
                if (sharedPreferences != null) {
                    String response = sharedPreferences.getString(Keys.USER_CREDENTIALS, null);
                    // Toast.makeText(getActivity(), "xml not null", Toast.LENGTH_LONG).show();
                    if (response != null && response.length() > 0) {
                        flag = true;
                    } else {
                        //  Toast.makeText(getApplicationContext(), "Data is not stored in xml", Toast.LENGTH_LONG);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            email.setText(user);
            password.setText(pass);

            if (flag) {
                Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(loginIntent);
                finish();
            }

        }
    }

    //--------------------------------------------GPS Tracker--------------------------------------------------------------

    @Override
    public void onLocationFound(Location location) {
        isLocationGet = true;
        gpsTrackerPresenter.stopLocationUpdates();
        if (isTxnClick) {
            isTxnClick = false;
            loginRequest();
        }
    }

    @Override
    public void locationError(String msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSTrackerPresenter.GPS_IS_ON__OR_OFF_CODE && resultCode == Activity.RESULT_OK) {
            gpsTrackerPresenter.onStart();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsTrackerPresenter.onStart();
    }

//--------------------------------------------End GPS Tracker--------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTrackerPresenter.onPause();
    }
}