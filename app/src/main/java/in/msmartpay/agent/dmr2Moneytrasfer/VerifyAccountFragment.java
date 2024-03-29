package in.msmartpay.agent.dmr2Moneytrasfer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.HttpURL;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Mysingleton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Smartkinda on 7/8/2017.
 */

public class VerifyAccountFragment extends Fragment {

    private EditText edit_sender_mobile, edit_bank_ifsc, edit_account_number, et_searchbank;
    private Spinner spinner_bank_name;
    private Button btnVerifyAcc;
    private ProgressDialog pd;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String agentID, txnKey, mobileNumber, spinnerSelectedBank, ifsc = "";
    private BankListModel listModel = null;
    private ArrayList<BankListModel> BankListArray = null;
    private CustomAdaptorClass bankListAdaptor;
    private String url_bank_list = HttpURL.GET_BANK_LIST_Dmr2;
    private String url_bank_details = HttpURL.BankDetails_Dmr2;
    private String url_add_bene_by_ifsc = HttpURL.ADD_BENEFICIARY_IFSC_CODE_Dmr2;
    private String isverificationavailable, available_channels, ifsc_status;

    private ArrayList<String> BanknameList = null;
    private ArrayList<String> BankcodeList = null;

    private String Bankname, Bankcode;

    private int position = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dmr2_verify_account_activity, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getActivity();
        sharedPreferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        mobileNumber = sharedPreferences.getString("ResisteredMobileNo", null);
        agentID = sharedPreferences.getString("agentonlyid", null);
        txnKey = sharedPreferences.getString("txn-key", null);

        edit_sender_mobile = view.findViewById(R.id.edit_sender_mobile);
        edit_bank_ifsc = view.findViewById(R.id.edit_bank_ifsc);
        edit_account_number = view.findViewById(R.id.edit_account_number);
        /* spinner_bank_name =  view.findViewById(R.id.spinner_bank_name);*/
        btnVerifyAcc = view.findViewById(R.id.btn_verify_account);
        edit_sender_mobile.setText(mobileNumber);
        // edit_bank_ifsc.setEnabled(false);

        et_searchbank = view.findViewById(R.id.et_searchbank);

        et_searchbank.setOnClickListener(v -> {

            if (BanknameList != null) {
                if (BanknameList.size() != 0) {
                    Intent intent = new Intent(context, Bank_name_search.class);
                    intent.putExtra("Bankname", BanknameList);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Bank List Not Available !!!", Toast.LENGTH_SHORT).show();
            }
        });

        /*BankList Request*/
        try {
            getBankListRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnVerifyAcc.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edit_sender_mobile.getText().toString().trim())) {
                edit_sender_mobile.requestFocus();
                Toast.makeText(context, "Please Enter Beneficiary Name!", Toast.LENGTH_SHORT).show();
            }/* else if (spinnerSelectedBank == null) {
                Toast.makeText(context, "Please Select Bank List!", Toast.LENGTH_LONG).show();
            } */ else if (TextUtils.isEmpty(edit_account_number.getText().toString().trim())) {
                edit_account_number.requestFocus();
                Toast.makeText(context, "Please Enter Account Number!", Toast.LENGTH_SHORT).show();
            } else {
                getBankDetails();
                //VerifyAccountRequest();
            }
        });

        return view;
    }

    //getBankListRequest
    private void getBankListRequest() throws JSONException {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        JSONObject jsonObjectReq = new JSONObject()
                .put("AgentID", agentID)
                .put("Key", txnKey)
                .put("SenderId", mobileNumber);

        L.m2("Request--banklist>", jsonObjectReq.toString());
        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bank_list,
                jsonObjectReq,
                object -> {
                    pd.dismiss();
                    System.out.println("object--banklist>" + object.toString());
                    try {
                        if (object.getString("Status").equalsIgnoreCase("0")) {
                            L.m2("Resp--banklist>", object.toString());
                            JSONArray stateJsonArray = object.getJSONArray("BankList");

                            BanknameList = new ArrayList<>();
                            BankcodeList = new ArrayList<>();
                            for (int i = 0; i < stateJsonArray.length(); i++) {

                                JSONObject obj = stateJsonArray.getJSONObject(i);

                                BanknameList.add(obj.getString("bname"));
                                BankcodeList.add(obj.getString("bcode"));

                            }

                        } else {
                            pd.dismiss();
                            //Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        pd.dismiss();
                        e.printStackTrace();
                    }
                }, error -> {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                });
        BaseActivity.getSocketTimeOut(jsonrequest);
        Mysingleton.getInstance(context).addToRequsetque(jsonrequest);

    }

    //Confirmation Dialog
    public void showConfirmationDialog(final int i, String msg) {
        // TODO Auto-generated method stub
        final Dialog d = new Dialog(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        d.setCancelable(false);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        d.setContentView(R.layout.dmr1_confirmation_dialog);

        final Button btnSubmit = d.findViewById(R.id.btn_push_submit);
        final Button btn_no = d.findViewById(R.id.close_push_button);
        final TextView tvConfirmation = (TextView) d.findViewById(R.id.tv_confirmation_dialog);

        if (i == 1) {
            tvConfirmation.setText("Are you sure, you want to enquiry!");
        }
        if (i == 2) {
            tvConfirmation.setText(msg);
        }
        btnSubmit.setOnClickListener(v -> {
            d.dismiss();
            if (i == 1) {
                VerifyAccountRequest();
            }

            if (i == 2) {
                try {
                    getBankListRequest();
                    edit_account_number.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_no.setOnClickListener(view -> d.dismiss());

        d.show();
    }

    //Json request for add Beneficiary
    private void VerifyAccountRequest() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("IFSC", edit_bank_ifsc.getText().toString())
                    .put("BankAccount", edit_account_number.getText().toString().trim())
                    .put("BankCode", Bankcode/*listModel.getBankCode()*/)
                    .put("BankName", Bankname/* spinnerSelectedBank*/)
                    .put("REQUEST_ID", String.valueOf((long) Math.floor(Math.random() * 90000000000000L) + 10000000000000L));

            L.m2("Request--byIfsc", jsonObjectReq.toString());
            L.m2("url", url_add_bene_by_ifsc);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_add_bene_by_ifsc, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("addBeneByIfsc-->" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {
                                    pd.dismiss();
                                    L.m2("url data--byIfsc", object.toString());
                                    String name = "";
                                    if (object.has("BeneName"))
                                        name="\n"+"Name :" + object.getString("BeneName");
                                    showConfirmationDialog(2, object.getString("message")+name);
                                } else {
                                    pd.dismiss();
                                    //Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            Bankname = data.getStringExtra("Bankname");
            position = BanknameList.indexOf(Bankname);
            Bankcode = BankcodeList.get(position);
            et_searchbank.setText(Bankname);
        } catch (Exception e) {
            Toast.makeText(context, "Please select your bank...", Toast.LENGTH_SHORT).show();
        }


    }

    //Json request for add Beneficiary
    private void getBankDetails() {
        pd = ProgressDialog.show(context, "", "Loading. Please wait...", true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        try {
            JSONObject jsonObjectReq = new JSONObject()
                    .put("AgentID", agentID)
                    .put("Key", txnKey)
                    .put("SenderId", mobileNumber)
                    .put("BankCode", Bankcode);
            L.m2("Request--byIfsc", jsonObjectReq.toString());
            L.m2("url", url_bank_details);
            JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, url_bank_details, jsonObjectReq,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            pd.dismiss();
                            System.out.println("addBeneByIfsc-->" + object.toString());
                            try {
                                pd.dismiss();
                                if (object.getString("Status").equalsIgnoreCase("0")) {

                                    isverificationavailable = object.getString("isverificationavailable");
                                    available_channels = object.getString("available_channels");
                                    ifsc_status = object.getString("ifsc_status");
                                    if ("1".equalsIgnoreCase(isverificationavailable) && "4".equalsIgnoreCase(ifsc_status)) {
                                        //  respMsg="IFSCRequired";
                                        if (TextUtils.isEmpty(edit_bank_ifsc.getText().toString())) {
                                            Toast.makeText(context, "Enter IFSC Code", Toast.LENGTH_SHORT).show();
                                        } else {
                                            showConfirmationDialog(1, "");
                                        }
                                    } else if ("1".equalsIgnoreCase(isverificationavailable) && !"4".equalsIgnoreCase(ifsc_status)) {
                                        // respMsg="Available";
                                        // Toast.makeText(context, "Available", Toast.LENGTH_SHORT).show();
                                        showConfirmationDialog(1, "");
                                    } else {
                                        Toast.makeText(context, "Bank not available for Account Verification.", Toast.LENGTH_SHORT).show();
                                        //  respMsg="Bank not available for Account Verification.";
                                    }
                                    //Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    //  respMsg="Bank status not Available.";
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                pd.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(context, "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            BaseActivity.getSocketTimeOut(jsonrequest);
            Mysingleton.getInstance(context).addToRequsetque(jsonrequest);
        } catch (JSONException e) {
            pd.dismiss();
            e.printStackTrace();
        }
    }


}
